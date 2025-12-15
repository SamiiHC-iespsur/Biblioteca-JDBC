package sami.ada.biblio.modelo;

import java.time.LocalDate;

public class Prestamo {

    private Integer codPrestamo;
    private LocalDate fecha;
    private LocalDate fechaDevolucion;
    private String idUsuario;

    public Prestamo(Integer codPrestamo, LocalDate fecha, LocalDate fechaDevolucion, String idUsuario) {
        this.codPrestamo = codPrestamo;
        this.fecha = fecha;
        this.fechaDevolucion = fechaDevolucion;
        this.idUsuario = idUsuario;
    }

    public Prestamo(LocalDate fecha, LocalDate fechaDevolucion, String idUsuario) {
        this(null, fecha, fechaDevolucion, idUsuario);
    }

    public Integer getCodPrestamo() {
        return codPrestamo;
    }

    public void setCodPrestamo(Integer codPrestamo) {
        this.codPrestamo = codPrestamo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
