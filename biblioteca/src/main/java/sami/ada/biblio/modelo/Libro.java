package sami.ada.biblio.modelo;

public class Libro {

    private Integer id;
    private String titulo;

    public Libro(Integer id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public Libro(String titulo) {
        this(null, titulo);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
