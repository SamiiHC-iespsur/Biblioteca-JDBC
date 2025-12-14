package sami.ada.biblio.modelo;

public class Escribe {

    private Integer idAutor;
    private Integer idLibro;

    public Escribe(Integer idAutor, Integer idLibro) {
        this.idAutor = idAutor;
        this.idLibro = idLibro;
    }

    public Integer getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(Integer idAutor) {
        this.idAutor = idAutor;
    }

    public Integer getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Integer idLibro) {
        this.idLibro = idLibro;
    }
}
