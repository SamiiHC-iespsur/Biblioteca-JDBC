package sami.ada.biblio.modelo.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sami.ada.biblio.modelo.Genero;

public class GeneroDAO {

    private final Conexion conexion;

    public GeneroDAO(Conexion conexion) {
        this.conexion = conexion;
    }

    public Genero insertar(Genero genero) throws SQLException {
        String sql = "INSERT INTO Genero (nombre_genero, id_libro) VALUES (?, ?)";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, genero.getNombreGenero());
            ps.setInt(2, genero.getIdLibro());
            ps.executeUpdate();
        }
        return genero;
    }

    public Optional<Genero> buscar(String nombreGenero, int idLibro) throws SQLException {
        String sql = "SELECT nombre_genero, id_libro FROM Genero WHERE nombre_genero = ? AND id_libro = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, nombreGenero);
            ps.setInt(2, idLibro);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Genero> listarPorLibro(int idLibro) throws SQLException {
        String sql = "SELECT nombre_genero, id_libro FROM Genero WHERE id_libro = ?";
        List<Genero> generos = new ArrayList<>();
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, idLibro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    generos.add(mapear(rs));
                }
            }
        }
        return generos;
    }

    public List<String> listarNombresDistinct() throws SQLException {
        String sql = "SELECT DISTINCT nombre_genero FROM Genero";
        List<String> nombres = new ArrayList<>();
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                nombres.add(rs.getString("nombre_genero"));
            }
        }
        return nombres;
    }

    public boolean eliminar(String nombreGenero, int idLibro) throws SQLException {
        String sql = "DELETE FROM Genero WHERE nombre_genero = ? AND id_libro = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, nombreGenero);
            ps.setInt(2, idLibro);
            return ps.executeUpdate() > 0;
        }
    }

    private Genero mapear(ResultSet rs) throws SQLException {
        return new Genero(
                rs.getString("nombre_genero"),
                rs.getInt("id_libro")
        );
    }
}
