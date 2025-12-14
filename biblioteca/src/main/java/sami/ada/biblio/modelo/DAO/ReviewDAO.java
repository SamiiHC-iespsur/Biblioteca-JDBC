package sami.ada.biblio.modelo.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sami.ada.biblio.modelo.Review;

public class ReviewDAO {

    private final Conexion conexion;

    public ReviewDAO(Conexion conexion) {
        this.conexion = conexion;
    }

    public Review insertar(Review review) throws SQLException {
        String sql = "INSERT INTO Review (dni_usuario, isbn_edicion, valoracion, descripcion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, review.getDniUsuario());
            ps.setString(2, review.getIsbnEdicion());
            ps.setInt(3, review.getValoracion());
            ps.setString(4, review.getDescripcion());
            ps.executeUpdate();
        }
        return review;
    }

    public Optional<Review> buscarPorId(String dniUsuario, String isbnEdicion) throws SQLException {
        String sql = "SELECT dni_usuario, isbn_edicion, valoracion, descripcion FROM Review WHERE dni_usuario = ? AND isbn_edicion = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, dniUsuario);
            ps.setString(2, isbnEdicion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Review> listarPorEdicion(String isbnEdicion) throws SQLException {
        String sql = "SELECT dni_usuario, isbn_edicion, valoracion, descripcion FROM Review WHERE isbn_edicion = ?";
        List<Review> reviews = new ArrayList<>();
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, isbnEdicion);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapear(rs));
                }
            }
        }
        return reviews;
    }

    public boolean actualizar(Review review) throws SQLException {
        String sql = "UPDATE Review SET valoracion = ?, descripcion = ? WHERE dni_usuario = ? AND isbn_edicion = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, review.getValoracion());
            ps.setString(2, review.getDescripcion());
            ps.setString(3, review.getDniUsuario());
            ps.setString(4, review.getIsbnEdicion());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String dniUsuario, String isbnEdicion) throws SQLException {
        String sql = "DELETE FROM Review WHERE dni_usuario = ? AND isbn_edicion = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, dniUsuario);
            ps.setString(2, isbnEdicion);
            return ps.executeUpdate() > 0;
        }
    }

    private Review mapear(ResultSet rs) throws SQLException {
        return new Review(
                rs.getString("dni_usuario"),
                rs.getString("isbn_edicion"),
                rs.getInt("valoracion"),
                rs.getString("descripcion")
        );
    }
}
