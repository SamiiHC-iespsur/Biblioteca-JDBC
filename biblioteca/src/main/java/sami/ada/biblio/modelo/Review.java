package sami.ada.biblio.modelo;

public class Review {

    private String dniUsuario;
    private String isbnEdicion;
    private Integer valoracion;
    private String descripcion;

    public Review(String dniUsuario, String isbnEdicion, Integer valoracion, String descripcion) {
        this.dniUsuario = dniUsuario;
        this.isbnEdicion = isbnEdicion;
        this.valoracion = valoracion;
        this.descripcion = descripcion;
    }

    public String getDniUsuario() {
        return dniUsuario;
    }

    public void setDniUsuario(String dniUsuario) {
        this.dniUsuario = dniUsuario;
    }

    public String getIsbnEdicion() {
        return isbnEdicion;
    }

    public void setIsbnEdicion(String isbnEdicion) {
        this.isbnEdicion = isbnEdicion;
    }

    public Integer getValoracion() {
        return valoracion;
    }

    public void setValoracion(Integer valoracion) {
        this.valoracion = valoracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
