package sami.ada.biblio.modelo;

public class Edicion {

    private String isbn;
    private Integer annoPublicacion;
    private String editorial;
    private String idioma;
    private Integer idLibro;

    public Edicion(String isbn, Integer annoPublicacion, String editorial, String idioma, Integer idLibro) {
        this.isbn = isbn;
        this.annoPublicacion = annoPublicacion;
        this.editorial = editorial;
        this.idioma = idioma;
        this.idLibro = idLibro;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getAnnoPublicacion() {
        return annoPublicacion;
    }

    public void setAnnoPublicacion(Integer annoPublicacion) {
        this.annoPublicacion = annoPublicacion;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Integer idLibro) {
        this.idLibro = idLibro;
    }
}
