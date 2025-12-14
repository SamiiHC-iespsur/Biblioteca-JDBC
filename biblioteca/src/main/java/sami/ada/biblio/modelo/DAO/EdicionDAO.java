package sami.ada.biblio.modelo.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sami.ada.biblio.modelo.Edicion;

public class EdicionDAO {

    private final Conexion conexion;

    public EdicionDAO(Conexion conexion) {
        this.conexion = conexion;
    }

    public Edicion insertar(Edicion edicion) throws SQLException {
        String sql = "INSERT INTO Edicion (isbn, anno_publicacion, editorial, idioma, id_libro) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, edicion.getIsbn());
            if (edicion.getAnnoPublicacion() != null) {
                ps.setInt(2, edicion.getAnnoPublicacion());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setString(3, edicion.getEditorial());
            ps.setString(4, edicion.getIdioma());
            ps.setInt(5, edicion.getIdLibro());
            ps.executeUpdate();
        }
        return edicion;
    }

    public Optional<Edicion> buscarPorIsbn(String isbn) throws SQLException {
        String sql = "SELECT isbn, anno_publicacion, editorial, idioma, id_libro FROM Edicion WHERE isbn = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Edicion> listarPorLibro(int idLibro) throws SQLException {
        String sql = "SELECT isbn, anno_publicacion, editorial, idioma, id_libro FROM Edicion WHERE id_libro = ?";
        List<Edicion> ediciones = new ArrayList<>();
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, idLibro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ediciones.add(mapear(rs));
                }
            }
        }
        return ediciones;
    }

    public boolean actualizar(Edicion edicion) throws SQLException {
        String sql = "UPDATE Edicion SET anno_publicacion = ?, editorial = ?, idioma = ?, id_libro = ? WHERE isbn = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            if (edicion.getAnnoPublicacion() != null) {
                ps.setInt(1, edicion.getAnnoPublicacion());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }
            ps.setString(2, edicion.getEditorial());
            ps.setString(3, edicion.getIdioma());
            ps.setInt(4, edicion.getIdLibro());
            ps.setString(5, edicion.getIsbn());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String isbn) throws SQLException {
        String sql = "DELETE FROM Edicion WHERE isbn = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, isbn);
            return ps.executeUpdate() > 0;
        }
    }

    private Edicion mapear(ResultSet rs) throws SQLException {
        return new Edicion(
                rs.getString("isbn"),
                rs.getObject("anno_publicacion") != null ? rs.getInt("anno_publicacion") : null,
                rs.getString("editorial"),
                rs.getString("idioma"),
                rs.getInt("id_libro")
        );
    }
}
