package sami.ada.biblio.modelo.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sami.ada.biblio.modelo.Libro;

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
