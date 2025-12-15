package sami.ada.biblio.modelo;

public class Asigna {

    private Integer idEjemplar;
    private Integer codPrestamo;

    public Asigna(Integer idEjemplar, Integer codPrestamo) {
        this.idEjemplar = idEjemplar;
        this.codPrestamo = codPrestamo;
    }

    public Integer getIdEjemplar() {
        return idEjemplar;
    }

    public void setIdEjemplar(Integer idEjemplar) {
        this.idEjemplar = idEjemplar;
    }

    public Integer getCodPrestamo() {
        return codPrestamo;
    }

    public void setCodPrestamo(Integer codPrestamo) {
        this.codPrestamo = codPrestamo;
    }
}
