package sami.ada.biblio.modelo.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sami.ada.biblio.modelo.Asigna;

public class AsignaDAO {

    private final Conexion conexion;

    public AsignaDAO(Conexion conexion) {
        this.conexion = conexion;
    }

    public Asigna insertar(Asigna asigna) throws SQLException {
        String sql = "INSERT INTO asigna (id_ejemplar, cod_prestamo) VALUES (?, ?)";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, asigna.getIdEjemplar());
            ps.setInt(2, asigna.getCodPrestamo());
            ps.executeUpdate();
        }
        return asigna;
    }

    public Optional<Asigna> buscar(int idEjemplar, int codPrestamo) throws SQLException {
        String sql = "SELECT id_ejemplar, cod_prestamo FROM asigna WHERE id_ejemplar = ? AND cod_prestamo = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, idEjemplar);
            ps.setInt(2, codPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Asigna> listarPorPrestamo(int codPrestamo) throws SQLException {
        String sql = "SELECT id_ejemplar, cod_prestamo FROM asigna WHERE cod_prestamo = ?";
        List<Asigna> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, codPrestamo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public List<Asigna> listarPorEjemplar(int idEjemplar) throws SQLException {
        String sql = "SELECT id_ejemplar, cod_prestamo FROM asigna WHERE id_ejemplar = ?";
        List<Asigna> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, idEjemplar);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public boolean eliminar(int idEjemplar, int codPrestamo) throws SQLException {
        String sql = "DELETE FROM asigna WHERE id_ejemplar = ? AND cod_prestamo = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, idEjemplar);
            ps.setInt(2, codPrestamo);
            return ps.executeUpdate() > 0;
        }
    }

    private Asigna mapear(ResultSet rs) throws SQLException {
        return new Asigna(rs.getInt("id_ejemplar"), rs.getInt("cod_prestamo"));
    }
}
