package hotel.entity;


import java.time.LocalDate;

public class Reserva {
    private Long id; // opcional si no usas base de datos
    private String codigoReserva;
    private String clienteId;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private String tipoHabitacion;
    private Double descuentoAplicado;

    // Constructor vacío
    public Reserva() {}

    // Constructor principal
    public Reserva(String clienteId, LocalDate fechaEntrada, LocalDate fechaSalida, String tipoHabitacion) {
        this.clienteId = clienteId;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.tipoHabitacion = tipoHabitacion;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoReserva() {
        return codigoReserva;
    }
    public void setCodigoReserva(String codigoReserva) {
        this.codigoReserva = codigoReserva;
    }

    public String getClienteId() {
        return clienteId;
    }
    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }
    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }
    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getTipoHabitacion() {
        return tipoHabitacion;
    }
    public void setTipoHabitacion(String tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public Double getDescuentoAplicado() {
        return descuentoAplicado;
    }
    public void setDescuentoAplicado(Double descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }
}
