package sami.ada.biblio.modelo;

public class Ejemplar {

    private Integer id;
    private Estado estado;
    private String isbnEdicion;

    public Ejemplar(Integer id, Estado estado, String isbnEdicion) {
        this.id = id;
        this.estado = estado;
        this.isbnEdicion = isbnEdicion;
    }

    public Ejemplar(Estado estado, String isbnEdicion) {
        this(null, estado, isbnEdicion);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getIsbnEdicion() {
        return isbnEdicion;
    }

    public void setIsbnEdicion(String isbnEdicion) {
        this.isbnEdicion = isbnEdicion;
    }

    public enum Estado {
        NUEVO("Nuevo"),
        BUENO("Bueno"),
        ACEPTABLE("Aceptable");

        private final String dbValue;

        Estado(String dbValue) {
            this.dbValue = dbValue;
        }

        public String getDbValue() {
            return dbValue;
        }

        public static Estado fromDbValue(String value) {
            if (value == null) {
                return null;
            }
            switch (value) {
                case "Nuevo" -> {
                    return NUEVO;
                }
                case "Bueno" -> {
                    return BUENO;
                }
                case "Aceptable" -> {
                    return ACEPTABLE;
                }
                default ->
                    throw new IllegalArgumentException("Estado de ejemplar no permitido: " + value);
            }
        }
    }
}
