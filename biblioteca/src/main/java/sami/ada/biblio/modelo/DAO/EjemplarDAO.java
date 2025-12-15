package sami.ada.biblio.modelo.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sami.ada.biblio.modelo.Ejemplar;
import sami.ada.biblio.modelo.Ejemplar.Estado;

public class EjemplarDAO {

    private final Conexion conexion;

    public EjemplarDAO(Conexion conexion) {
        this.conexion = conexion;
    }

    public Ejemplar insertar(Ejemplar ejemplar) throws SQLException {
        String sql = "INSERT INTO Ejemplar (estado, isbn_edicion) VALUES (?, ?)";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ejemplar.getEstado() != null ? ejemplar.getEstado().getDbValue() : null);
            ps.setString(2, ejemplar.getIsbnEdicion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    ejemplar.setId(rs.getInt(1));
                }
            }
        }
        return ejemplar;
    }

    public Optional<Ejemplar> buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, estado, isbn_edicion FROM Ejemplar WHERE id = ?";
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

    public List<Ejemplar> listarPorIsbn(String isbnEdicion) throws SQLException {
        String sql = "SELECT id, estado, isbn_edicion FROM Ejemplar WHERE isbn_edicion = ?";
        List<Ejemplar> ejemplares = new ArrayList<>();
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, isbnEdicion);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ejemplares.add(mapear(rs));
                }
            }
        }
        return ejemplares;
    }

    public boolean actualizar(Ejemplar ejemplar) throws SQLException {
        String sql = "UPDATE Ejemplar SET estado = ?, isbn_edicion = ? WHERE id = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, ejemplar.getEstado() != null ? ejemplar.getEstado().getDbValue() : null);
            ps.setString(2, ejemplar.getIsbnEdicion());
            ps.setInt(3, ejemplar.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Ejemplar WHERE id = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Ejemplar mapear(ResultSet rs) throws SQLException {
        return new Ejemplar(
                rs.getInt("id"),
                Estado.fromDbValue(rs.getString("estado")),
                rs.getString("isbn_edicion")
        );
    }
}
