package sami.ada.biblio.modelo.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sami.ada.biblio.modelo.Autor;

public class AutorDAO {

    private final Conexion conexion;

    public AutorDAO(Conexion conexion) {
        this.conexion = conexion;
    }

    public Autor insertar(Autor autor) throws SQLException {
        String sql = "INSERT INTO Autor (nombre, anno_nacimiento, nacionalidad) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, autor.getNombre());
            if (autor.getAnnoNacimiento() != null) {
                ps.setInt(2, autor.getAnnoNacimiento());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setString(3, autor.getNacionalidad());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    autor.setId(rs.getInt(1));
                }
            }
        }
        return autor;
    }

    public Optional<Autor> buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, anno_nacimiento, nacionalidad FROM Autor WHERE id = ?";
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

    public List<Autor> listarTodos() throws SQLException {
        String sql = "SELECT id, nombre, anno_nacimiento, nacionalidad FROM Autor";
        List<Autor> autores = new ArrayList<>();
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                autores.add(mapear(rs));
            }
        }
        return autores;
    }

    public boolean actualizar(Autor autor) throws SQLException {
        String sql = "UPDATE Autor SET nombre = ?, anno_nacimiento = ?, nacionalidad = ? WHERE id = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, autor.getNombre());
            if (autor.getAnnoNacimiento() != null) {
                ps.setInt(2, autor.getAnnoNacimiento());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            ps.setString(3, autor.getNacionalidad());
            ps.setInt(4, autor.getId());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Autor WHERE id = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Autor mapear(ResultSet rs) throws SQLException {
        return new Autor(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getObject("anno_nacimiento") != null ? rs.getInt("anno_nacimiento") : null,
                rs.getString("nacionalidad")
        );
    }
}
