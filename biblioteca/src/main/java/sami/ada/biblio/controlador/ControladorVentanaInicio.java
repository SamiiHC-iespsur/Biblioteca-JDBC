package sami.ada.biblio.controlador;

import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import sami.ada.biblio.modelo.DAO.Conexion;
import sami.ada.biblio.modelo.DAO.EdicionDAO;
import sami.ada.biblio.modelo.DAO.EjemplarDAO;
import sami.ada.biblio.modelo.DAO.GeneroDAO;
import sami.ada.biblio.modelo.DAO.LibroDAO;
import sami.ada.biblio.modelo.DAO.ReviewDAO;
import sami.ada.biblio.modelo.DAO.UsuarioDAO;
import sami.ada.biblio.modelo.Edicion;
import sami.ada.biblio.modelo.EdicionConLibro;
import sami.ada.biblio.vista.Detalles;
import sami.ada.biblio.vista.Inicio;
import sami.ada.biblio.vista.Login;

public class ControladorVentanaInicio {

    private final Inicio vista;
    private final GeneroDAO generoDAO;
    private final LibroDAO libroDAO;
    private final EdicionDAO edicionDAO;
    private final EjemplarDAO ejemplarDAO;
    private final ReviewDAO reviewDAO;
    private final UsuarioDAO usuarioDAO;
    private final Conexion conexion;
    private List<EdicionConLibro> ediciones;
    private String filtroGenero;
    private String filtroBusqueda;

    public ControladorVentanaInicio(Inicio vista, GeneroDAO generoDAO, LibroDAO libroDAO, EdicionDAO edicionDAO, EjemplarDAO ejemplarDAO, ReviewDAO reviewDAO, UsuarioDAO usuarioDAO, Conexion conexion) {
        this.vista = vista;
        this.generoDAO = generoDAO;
        this.libroDAO = libroDAO;
        this.edicionDAO = edicionDAO;
        this.ejemplarDAO = ejemplarDAO;
        this.reviewDAO = reviewDAO;
        this.usuarioDAO = usuarioDAO;
        this.conexion = conexion;
    }

    public void iniciar() {
        registrarEventos();
        cargarGeneros();
        vista.setOnEdicionSeleccionada(this::abrirDetalles);
        vista.setOnGeneroSeleccionado(this::filtrarPorGenero);
        vista.setOnBusquedaCambiada(this::filtrarPorTexto);
        cargarEdiciones();
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    private void registrarEventos() {
        vista.getBotonCerrarSesion().addActionListener(e -> cerrarSesion());
    }

    private void cargarGeneros() {
        try {
            List<String> generos = generoDAO.listarNombresDistinct();
            vista.mostrarGeneros(generos);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "No se pudieron cargar los géneros", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarEdiciones() {
        try {
            ediciones = edicionDAO.listarEdicionesConLibro();
            aplicarFiltros();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "No se pudieron cargar las ediciones", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filtrarPorGenero(String genero) {
        filtroGenero = genero;
        aplicarFiltros();
    }

    private void filtrarPorTexto(String texto) {
        filtroBusqueda = texto != null ? texto.toLowerCase() : null;
        aplicarFiltros();
    }

    private void aplicarFiltros() {
        if (ediciones == null) {
            return;
        }

        List<EdicionConLibro> filtradas = ediciones.stream()
                .filter(this::pasaFiltroGenero)
                .filter(this::pasaFiltroBusqueda)
                .toList();

        vista.mostrarEdiciones(filtradas);
    }

    private boolean pasaFiltroBusqueda(EdicionConLibro edicion) {
        if (filtroBusqueda == null || filtroBusqueda.isBlank()) {
            return true;
        }
        String titulo = edicion.getTitulo() != null ? edicion.getTitulo().toLowerCase() : "";
        return titulo.contains(filtroBusqueda);
    }

    private boolean pasaFiltroGenero(EdicionConLibro edicion) {
        if (filtroGenero == null || filtroGenero.isBlank()) {
            return true;
        }
        Integer idLibro = edicion.getIdLibro();
        if (idLibro == null) {
            return false;
        }
        try {
            return generoDAO.listarPorLibro(idLibro).stream()
                    .anyMatch(g -> filtroGenero.equalsIgnoreCase(g.getNombreGenero()));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "No se pudieron filtrar por género", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
    }

    private void abrirDetalles(EdicionConLibro edicionUI) {
        try {
            if (edicionUI.getIsbn() == null || edicionUI.getIsbn().isBlank()) {
                JOptionPane.showMessageDialog(vista, "No hay edición asociada", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Edicion edicion = edicionDAO.buscarPorIsbn(edicionUI.getIsbn()).orElse(null);
            if (edicion == null) {
                JOptionPane.showMessageDialog(vista, "No se encontraron datos de la edición", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Detalles detalles = new Detalles(vista.getUsuario());
            ControladorVentanaDetalles controladorDetalles = new ControladorVentanaDetalles(detalles, generoDAO, ejemplarDAO, reviewDAO, usuarioDAO);
            controladorDetalles.cargar(edicionUI, edicion);
            controladorDetalles.configurarNavegacion(detalles::dispose, () -> {
                detalles.dispose();
                cerrarSesion();
            });
            controladorDetalles.configurarResennas();
            detalles.setLocationRelativeTo(vista);
            detalles.setVisible(true);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al cargar detalles de la edición", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarSesion() {
        vista.dispose();
        conexion.cerrarConexion();
        Login login = new Login();
        login.setVisible(true);
    }
}
