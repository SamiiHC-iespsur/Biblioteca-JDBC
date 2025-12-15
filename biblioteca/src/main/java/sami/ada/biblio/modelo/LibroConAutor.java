package sami.ada.biblio.modelo;

public class LibroConAutor {

    private final Integer id;
    private final String titulo;
    private final String autor;
    private final String isbnEdicion;

    public LibroConAutor(Integer id, String titulo, String autor, String isbnEdicion) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbnEdicion = isbnEdicion;
    }

    public Integer getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbnEdicion() {
        return isbnEdicion;
    }
}
