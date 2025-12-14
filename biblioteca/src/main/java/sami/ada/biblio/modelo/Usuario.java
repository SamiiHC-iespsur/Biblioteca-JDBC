package sami.ada.biblio.modelo;

public class Usuario {

    private String dni;
    private String nombre;
    private String dirCorreo;
    private String pswd;

    public Usuario(String dni, String nombre, String dirCorreo, String pswd) {
        this.dni = dni;
        this.nombre = nombre;
        this.dirCorreo = dirCorreo;
        this.pswd = pswd;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDirCorreo() {
        return dirCorreo;
    }

    public void setDirCorreo(String dirCorreo) {
        this.dirCorreo = dirCorreo;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }
}
