package sami.ada.biblio.modelo.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sami.ada.biblio.modelo.Escribe;

public class EscribeDAO {

    private final Conexion conexion;

    public EscribeDAO(Conexion conexion) {
        this.conexion = conexion;
    }

    public Escribe insertar(Escribe escribe) throws SQLException {
        String sql = "INSERT INTO Escribe (id_autor, id_libro) VALUES (?, ?)";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, escribe.getIdAutor());
            ps.setInt(2, escribe.getIdLibro());
            ps.executeUpdate();
        }
        return escribe;
    }

    public Optional<Escribe> buscar(int idAutor, int idLibro) throws SQLException {
        String sql = "SELECT id_autor, id_libro FROM Escribe WHERE id_autor = ? AND id_libro = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, idAutor);
            ps.setInt(2, idLibro);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Escribe> listarPorAutor(int idAutor) throws SQLException {
        String sql = "SELECT id_autor, id_libro FROM Escribe WHERE id_autor = ?";
        List<Escribe> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, idAutor);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public List<Escribe> listarPorLibro(int idLibro) throws SQLException {
        String sql = "SELECT id_autor, id_libro FROM Escribe WHERE id_libro = ?";
        List<Escribe> lista = new ArrayList<>();
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, idLibro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public boolean eliminar(int idAutor, int idLibro) throws SQLException {
        String sql = "DELETE FROM Escribe WHERE id_autor = ? AND id_libro = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, idAutor);
            ps.setInt(2, idLibro);
            return ps.executeUpdate() > 0;
        }
    }

    private Escribe mapear(ResultSet rs) throws SQLException {
        return new Escribe(rs.getInt("id_autor"), rs.getInt("id_libro"));
    }
}
