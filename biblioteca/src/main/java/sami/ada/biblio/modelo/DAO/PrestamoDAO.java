package sami.ada.biblio.modelo.DAO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sami.ada.biblio.modelo.Prestamo;

public class PrestamoDAO {

    private final Conexion conexion;

    public PrestamoDAO(Conexion conexion) {
        this.conexion = conexion;
    }

    public Prestamo insertar(Prestamo prestamo) throws SQLException {
        String sql = "INSERT INTO Prestamo (cod_prestamo, fecha, fecha_devolucion, id_usuario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, prestamo.getCodPrestamo());
            ps.setDate(2, prestamo.getFecha() != null ? Date.valueOf(prestamo.getFecha()) : null);
            ps.setDate(3, prestamo.getFechaDevolucion() != null ? Date.valueOf(prestamo.getFechaDevolucion()) : null);
            ps.setString(4, prestamo.getIdUsuario());
            ps.executeUpdate();
        }
        return prestamo;
    }

    public Optional<Prestamo> buscarPorCodigo(int codPrestamo) throws SQLException {
        String sql = "SELECT cod_prestamo, fecha, fecha_devolucion, id_usuario FROM Prestamo WHERE cod_prestamo = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, codPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Prestamo> listarPorUsuario(String dniUsuario) throws SQLException {
        String sql = "SELECT cod_prestamo, fecha, fecha_devolucion, id_usuario FROM Prestamo WHERE id_usuario = ?";
        List<Prestamo> prestamos = new ArrayList<>();
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, dniUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    prestamos.add(mapear(rs));
                }
            }
        }
        return prestamos;
    }

    public boolean actualizar(Prestamo prestamo) throws SQLException {
        String sql = "UPDATE Prestamo SET fecha = ?, fecha_devolucion = ?, id_usuario = ? WHERE cod_prestamo = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setDate(1, prestamo.getFecha() != null ? Date.valueOf(prestamo.getFecha()) : null);
            ps.setDate(2, prestamo.getFechaDevolucion() != null ? Date.valueOf(prestamo.getFechaDevolucion()) : null);
            ps.setString(3, prestamo.getIdUsuario());
            ps.setInt(4, prestamo.getCodPrestamo());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int codPrestamo) throws SQLException {
        String sql = "DELETE FROM Prestamo WHERE cod_prestamo = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, codPrestamo);
            return ps.executeUpdate() > 0;
        }
    }

    private Prestamo mapear(ResultSet rs) throws SQLException {
        return new Prestamo(
                rs.getInt("cod_prestamo"),
                rs.getDate("fecha") != null ? rs.getDate("fecha").toLocalDate() : null,
                rs.getDate("fecha_devolucion") != null ? rs.getDate("fecha_devolucion").toLocalDate() : null,
                rs.getString("id_usuario")
        );
    }
}
