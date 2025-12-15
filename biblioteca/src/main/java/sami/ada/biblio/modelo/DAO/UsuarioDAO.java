package sami.ada.biblio.modelo.DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import sami.ada.biblio.modelo.Usuario;

public class UsuarioDAO {

    private final Conexion conexion;

    public UsuarioDAO(Conexion conexion) {
        this.conexion = conexion;
    }

    public Usuario insertar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO Usuario (dni, nombre, dir_correo, pswd) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, usuario.getDni());
            ps.setString(2, usuario.getNombre());
            ps.setString(3, usuario.getDirCorreo());
            ps.setString(4, usuario.getPswd());
            ps.executeUpdate();
        }
        return usuario;
    }

    public Optional<Usuario> buscarPorDni(String dni) throws SQLException {
        String sql = "SELECT dni, nombre, dir_correo, pswd FROM Usuario WHERE dni = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, dni);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT dni, nombre, dir_correo, pswd FROM Usuario";
        List<Usuario> usuarios = new ArrayList<>();
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(mapear(rs));
            }
        }
        return usuarios;
    }

    public boolean actualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE Usuario SET nombre = ?, dir_correo = ?, pswd = ? WHERE dni = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getDirCorreo());
            ps.setString(3, usuario.getPswd());
            ps.setString(4, usuario.getDni());
            return ps.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String dni) throws SQLException {
        String sql = "DELETE FROM Usuario WHERE dni = ?";
        try (PreparedStatement ps = conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, dni);
            return ps.executeUpdate() > 0;
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getString("dni"),
                rs.getString("nombre"),
                rs.getString("dir_correo"),
                rs.getString("pswd")
        );
    }
}
