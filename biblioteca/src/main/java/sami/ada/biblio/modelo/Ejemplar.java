package sami.ada.biblio.modelo;

public class Ejemplar {

    private Integer id;
    private String estado;
    private String isbnEdicion;

    public Ejemplar(Integer id, String estado, String isbnEdicion) {
        this.id = id;
        this.estado = estado;
        this.isbnEdicion = isbnEdicion;
    }

    public Ejemplar(String estado, String isbnEdicion) {
        this(null, estado, isbnEdicion);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getIsbnEdicion() {
        return isbnEdicion;
    }

    public void setIsbnEdicion(String isbnEdicion) {
        this.isbnEdicion = isbnEdicion;
    }
}
