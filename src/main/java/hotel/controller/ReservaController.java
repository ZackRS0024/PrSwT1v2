package hotel.controller;

import hotel.entity.Reserva;
import hotel.service.ReservaService;

import java.time.LocalDate;

public class ReservaController {
    private ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    public String crearReserva(String clienteId, LocalDate entrada, LocalDate salida, String tipoHabitacion) {
        try {
            Reserva reserva = new Reserva(clienteId, entrada, salida, tipoHabitacion);
            reservaService.registrarReserva(reserva);
            return "Reserva creada con éxito. Código: " + reserva.getCodigoReserva();
        } catch (Exception e) {
            return "Error al crear reserva: " + e.getMessage();
        }
    }
}
