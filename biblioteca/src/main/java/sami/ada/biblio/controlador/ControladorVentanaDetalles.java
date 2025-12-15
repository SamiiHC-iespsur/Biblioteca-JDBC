package sami.ada.biblio.controlador;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import sami.ada.biblio.modelo.DAO.EjemplarDAO;
import sami.ada.biblio.modelo.DAO.GeneroDAO;
import sami.ada.biblio.modelo.DAO.ReviewDAO;
import sami.ada.biblio.modelo.DAO.UsuarioDAO;
import sami.ada.biblio.modelo.Edicion;
import sami.ada.biblio.modelo.EdicionConLibro;
import sami.ada.biblio.modelo.Ejemplar;
import sami.ada.biblio.modelo.Ejemplar.Estado;
import sami.ada.biblio.modelo.Review;
import sami.ada.biblio.vista.Detalles;

public class ControladorVentanaDetalles {

    private final Detalles vista;
    private final GeneroDAO generoDAO;
    private final EjemplarDAO ejemplarDAO;
    private final ReviewDAO reviewDAO;
    private final UsuarioDAO usuarioDAO;

    private String isbnActual;

    public ControladorVentanaDetalles(Detalles vista, GeneroDAO generoDAO, EjemplarDAO ejemplarDAO, ReviewDAO reviewDAO, UsuarioDAO usuarioDAO) {
        this.vista = vista;
        this.generoDAO = generoDAO;
        this.ejemplarDAO = ejemplarDAO;
        this.reviewDAO = reviewDAO;
        this.usuarioDAO = usuarioDAO;
    }

    public void cargar(EdicionConLibro edicionUI, Edicion edicion) throws SQLException {
        vista.setTitulo(edicionUI.getTitulo());
        vista.setAutoria(edicionUI.getAutores());
        vista.setEditorial(edicion.getEditorial());
        vista.setGeneros(obtenerGeneros(edicion.getIdLibro()));
        vista.setMejorEstado(obtenerMejorEstado(edicion.getIsbn()));
        vista.setIsbn(edicion.getIsbn());
        vista.setCubierta(cargarPortada(edicionUI.getTitulo()));
        isbnActual = edicion.getIsbn();
        cargarReviews();
    }

    public void configurarNavegacion(Runnable onVolver, Runnable onCerrarSesion) {
        vista.getBotonVolver().addActionListener(e -> {
            if (onVolver != null) {
                onVolver.run();
            }
        });

        vista.getBotonCerrarSesion().addActionListener(e -> {
            if (onCerrarSesion != null) {
                onCerrarSesion.run();
            }
        });
    }

    public void configurarResennas() {
        vista.getBotonEnviarResenna().addActionListener(e -> enviarResenna());
    }

    private String obtenerGeneros(Integer idLibro) throws SQLException {
        if (idLibro == null) {
            return "";
        }
        List<String> generos = generoDAO.listarPorLibro(idLibro).stream()
                .map(g -> g.getNombreGenero())
                .collect(Collectors.toList());
        return generos.isEmpty() ? "Ninguno" : String.join(", ", generos);
    }

    private String obtenerMejorEstado(String isbnEdicion) throws SQLException {
        if (isbnEdicion == null || isbnEdicion.isBlank()) {
            return "Ninguno";
        }

        List<Ejemplar> ejemplares = ejemplarDAO.listarPorIsbn(isbnEdicion);
        Optional<Estado> mejorEstado = seleccionarMejorEstado(ejemplares);
        return mejorEstado.map(this::formatearEstado).orElse("Ninguno");
    }

    private void cargarReviews() throws SQLException {
        if (isbnActual == null || isbnActual.isBlank()) {
            vista.mostrarReviews(List.of());
            vista.mostrarMediaValoracion(0);
            return;
        }

        List<Review> reviews = reviewDAO.listarPorEdicion(isbnActual);
        java.util.Map<String, String> nombres = new java.util.HashMap<>();
        for (Review r : reviews) {
            String dni = r.getDniUsuario();
            if (dni != null && !nombres.containsKey(dni)) {
                usuarioDAO.buscarPorDni(dni).ifPresent(u -> nombres.put(dni, u.getNombre()));
            }
        }

        for (Review r : reviews) {
            String dni = r.getDniUsuario();
            String nombre = dni != null ? nombres.get(dni) : null;
            if (nombre != null && !nombre.isBlank()) {
                r.setDniUsuario(nombre);
            }
        }

        vista.mostrarReviews(reviews);
        double media = reviews.isEmpty() ? 0.0 : reviews.stream()
                .mapToInt(r -> r.getValoracion() != null ? r.getValoracion() : 0)
                .average()
                .orElse(0.0);
        vista.mostrarMediaValoracion(media);
    }

    private void enviarResenna() {
        try {
            if (isbnActual == null || isbnActual.isBlank()) {
                JOptionPane.showMessageDialog(vista, "No hay edición seleccionada", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            var usuario = vista.getUsuario();
            if (usuario == null || usuario.getDni() == null || usuario.getDni().isBlank()) {
                JOptionPane.showMessageDialog(vista, "No se ha podido identificar al usuario", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String textoPuntuacion = vista.getCampoPuntuacion().getText();
            int puntuacion;
            try {
                puntuacion = Integer.parseInt(textoPuntuacion);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(vista, "La puntuación debe ser un número entero", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (puntuacion < 1 || puntuacion > 5) {
                JOptionPane.showMessageDialog(vista, "La puntuación debe estar entre 1 y 5", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String comentario = vista.getCampoComentario().getText();
            Review review = new Review(usuario.getDni(), isbnActual, puntuacion, comentario);

            Optional<Review> existente = reviewDAO.buscarPorId(usuario.getDni(), isbnActual);
            if (existente.isPresent()) {
                reviewDAO.actualizar(review);
            } else {
                reviewDAO.insertar(review);
            }

            cargarReviews();
            vista.limpiarFormularioReview();
            JOptionPane.showMessageDialog(vista, "Reseña guardada", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "No se pudo guardar la reseña", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Optional<Estado> seleccionarMejorEstado(List<Ejemplar> ejemplares) {
        if (ejemplares == null || ejemplares.isEmpty()) {
            return Optional.empty();
        }

        for (Estado preferencia : new Estado[]{Estado.NUEVO, Estado.BUENO, Estado.ACEPTABLE}) {
            boolean disponible = ejemplares.stream().anyMatch(e -> preferencia.equals(e.getEstado()));
            if (disponible) {
                return Optional.of(preferencia);
            }
        }
        return Optional.empty();
    }

    private String formatearEstado(Estado estado) {
        switch (estado) {
            case NUEVO:
                return "Nuevo";
            case BUENO:
                return "Bueno";
            case ACEPTABLE:
                return "Aceptable";
            default:
                return "";
        }
    }

    private ImageIcon cargarPortada(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            return null;
        }

        String nombreNormalizado = normalizarNombreImagen(titulo);
        String nombreArchivo = nombreNormalizado + ".png";

        // Primero intentamos cargarlo como recurso de clase (ideal cuando las imágenes están en src/main/resources)
        String recursoClasspath = "/sami/ada/biblio/vista/imgs/" + nombreArchivo;
        java.net.URL recurso = getClass().getResource(recursoClasspath);
        if (recurso != null) {
            return new ImageIcon(recurso);
        }

        // Fallback: buscamos en rutas relativas habituales para entorno de desarrollo
        Path[] candidatos = new Path[]{
            Paths.get("biblioteca", "src", "main", "resources", "sami", "ada", "biblio", "vista", "imgs", nombreArchivo),
            Paths.get("biblioteca", "src", "main", "java", "sami", "ada", "biblio", "vista", "imgs", nombreArchivo),
            Paths.get("src", "main", "resources", "sami", "ada", "biblio", "vista", "imgs", nombreArchivo),
            Paths.get("src", "main", "java", "sami", "ada", "biblio", "vista", "imgs", nombreArchivo)
        };

        for (Path candidato : candidatos) {
            Path absoluto = candidato.toAbsolutePath();
            if (Files.exists(absoluto)) {
                return new ImageIcon(absoluto.toString());
            }
        }

        return null;
    }

    private String normalizarNombreImagen(String titulo) {
        String base = titulo.toLowerCase(Locale.ROOT).replace("ñ", "nn");
        String sinTildes = Normalizer.normalize(base, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        return sinTildes
                .replace(' ', '_')
                .replaceAll("[^a-z0-9_]", "");
    }
}
