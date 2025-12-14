package sami.ada.biblio.modelo;

public class Autor {

    private Integer id;
    private String nombre;
    private Integer annoNacimiento;
    private String nacionalidad;

    public Autor(Integer id, String nombre, Integer annoNacimiento, String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.annoNacimiento = annoNacimiento;
        this.nacionalidad = nacionalidad;
    }

    public Autor(String nombre, Integer annoNacimiento, String nacionalidad) {
        this(null, nombre, annoNacimiento, nacionalidad);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getAnnoNacimiento() {
        return annoNacimiento;
    }

    public void setAnnoNacimiento(Integer annoNacimiento) {
        this.annoNacimiento = annoNacimiento;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }
}
