package sami.ada.biblio.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import sami.ada.biblio.modelo.DAO.Conexion;
import sami.ada.biblio.modelo.DAO.EdicionDAO;
import sami.ada.biblio.modelo.DAO.EjemplarDAO;
import sami.ada.biblio.modelo.DAO.GeneroDAO;
import sami.ada.biblio.modelo.DAO.LibroDAO;
import sami.ada.biblio.modelo.DAO.ReviewDAO;
import sami.ada.biblio.modelo.DAO.UsuarioDAO;
import sami.ada.biblio.modelo.Usuario;
import sami.ada.biblio.vista.Inicio;
import sami.ada.biblio.vista.Login;

public class ControladorVentanaLogin implements ActionListener {

    private static final Logger logger = Logger.getLogger(ControladorVentanaLogin.class.getName());

    private final Login vista;
    private final UsuarioDAO usuarioDAO;
    private final Conexion conexion;

    public ControladorVentanaLogin(Login vista, UsuarioDAO usuarioDAO, Conexion conexion) {
        this.vista = vista;
        this.usuarioDAO = usuarioDAO;
        this.conexion = conexion;
    }

    public void registrarEventos() {
        vista.getBotonLogin().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBotonLogin()) {
            intentarLogin(vista.getDni(), vista.getContrasena());
        }
    }

    private void intentarLogin(String dni, char[] contrasenna) {
        try {
            if (!validarLocal(dni, contrasenna)) {
                return;
            }

            Usuario usuario = validarRemoto(dni, contrasenna);
            if (usuario == null) {
                return;
            }

            GeneroDAO generoDAO = new GeneroDAO(conexion);
            LibroDAO libroDAO = new LibroDAO(conexion);
            EdicionDAO edicionDAO = new EdicionDAO(conexion);
            EjemplarDAO ejemplarDAO = new EjemplarDAO(conexion);
            ReviewDAO reviewDAO = new ReviewDAO(conexion);
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);

            Inicio inicio = new Inicio(usuario);
            ControladorVentanaInicio controladorInicio = new ControladorVentanaInicio(inicio, generoDAO, libroDAO, edicionDAO, ejemplarDAO, reviewDAO, usuarioDAO, conexion);
            controladorInicio.iniciar();

            vista.cerrar();
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al validar credenciales", ex);
            vista.mostrarMensajeError("Error al validar las credenciales. Inténtalo de nuevo.");
        } finally {
            Arrays.fill(contrasenna, '\0');
        }
    }

    private boolean validarLocal(String dni, char[] contrasenna) {
        if (dni == null || !dni.matches("[0-9]{8}[A-Za-z]")) {
            vista.mostrarMensajeError("El DNI no es válido.");
            return false;
        }

        if (contrasenna == null || contrasenna.length == 0) {
            vista.mostrarMensajeError("La contraseña no puede estar vacía.");
            return false;
        }

        return true;
    }

    private Usuario validarRemoto(String dni, char[] contrasenna) throws SQLException {
        var posibleUsuario = usuarioDAO.buscarPorDni(dni);
        if (posibleUsuario.isEmpty()) {
            vista.mostrarMensajeError("DNI o contraseña incorrectos.");
            return null;
        }

        Usuario usuario = posibleUsuario.get();
        String pswd = usuario.getPswd();
        if (pswd == null || !pswd.equals(new String(contrasenna))) {
            vista.mostrarMensajeError("DNI o contraseña incorrectos.");
            return null;
        }

        return usuario;
    }
}
