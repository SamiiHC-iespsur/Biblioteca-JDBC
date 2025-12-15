package sami.ada.biblio.modelo.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sami.ada.biblio.modelo.Libro;
import sami.ada.biblio.modelo.LibroConAutor;

public class LibroDAO {

    private final Conexion conexion;

    public LibroDAO(Conexion conexion) {
        this.conexion = conexion;
    }

    public Libro insertar(Libro libro) throws SQLException {
        String sql = "INSERT INTO Libro (titulo) VALUES (?)";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, libro.getTitulo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    libro.setId(rs.getInt(1));
                }
            }
        }
        return libro;
    }

    public Optional<Libro> buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, titulo FROM Libro WHERE id = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Libro> listarTodos() throws SQLException {
        String sql = "SELECT id, titulo FROM Libro";
        List<Libro> libros = new ArrayList<>();
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                libros.add(mapear(rs));
            }
        }
        return libros;
    }

    public List<LibroConAutor> listarConAutores() throws SQLException {
        String sql = "SELECT l.id, l.titulo, "
                + "GROUP_CONCAT(a.nombre SEPARATOR ', ') AS autores, "
                + "(SELECT e2.isbn FROM Edicion e2 WHERE e2.id_libro = l.id ORDER BY e2.isbn LIMIT 1) AS isbn "
                + "FROM Libro l "
                + "LEFT JOIN Escribe e ON e.id_libro = l.id "
                + "LEFT JOIN Autor a ON a.id = e.id_autor "
                + "GROUP BY l.id, l.titulo "
                + "ORDER BY l.titulo";

        List<LibroConAutor> libros = new ArrayList<>();
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String autores = rs.getString("autores");
                libros.add(new LibroConAutor(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        autores != null && !autores.isBlank() ? autores : "Autor desconocido",
                        rs.getString("isbn")));
            }
        }
        return libros;
    }

    public boolean actualizar(Libro libro) throws SQLException {
        String sql = "UPDATE Libro SET titulo = ? WHERE id = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, libro.getTitulo());
            ps.setInt(2, libro.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Libro WHERE id = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Libro mapear(ResultSet rs) throws SQLException {
        return new Libro(rs.getInt("id"), rs.getString("titulo"));
    }
}
