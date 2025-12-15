package sami.ada.biblio.modelo;

public class EdicionConLibro {

    private final String isbn;
    private final Integer idLibro;
    private final String titulo;
    private final String autores;

    public EdicionConLibro(String isbn, Integer idLibro, String titulo, String autores) {
        this.isbn = isbn;
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.autores = autores;
    }

    public String getIsbn() {
        return isbn;
    }

    public Integer getIdLibro() {
        return idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutores() {
        return autores;
    }
}
