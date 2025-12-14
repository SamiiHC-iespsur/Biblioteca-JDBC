package sami.ada.biblio.modelo;

public class Genero {

    private String nombreGenero;
    private Integer idLibro;

    public Genero(String nombreGenero, Integer idLibro) {
        this.nombreGenero = nombreGenero;
        this.idLibro = idLibro;
    }

    public String getNombreGenero() {
        return nombreGenero;
    }

    public void setNombreGenero(String nombreGenero) {
        this.nombreGenero = nombreGenero;
    }

    public Integer getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Integer idLibro) {
        this.idLibro = idLibro;
    }
}
