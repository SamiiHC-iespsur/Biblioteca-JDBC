package sami.ada.biblio.principal;

import sami.ada.biblio.controlador.ControladorVentanaLogin;
import sami.ada.biblio.modelo.DAO.Conexion;
import sami.ada.biblio.modelo.DAO.UsuarioDAO;
import sami.ada.biblio.vista.Login;

public class Main {

    public static void main(String[] args) {
        Conexion conexion = new Conexion();
        UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);

        Login login = new Login();
        ControladorVentanaLogin controlador = new ControladorVentanaLogin(login, usuarioDAO, conexion);
        controlador.registrarEventos();

        login.setVisible(true);
    }
}
