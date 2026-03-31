package hotel.service;

import hotel.entity.Reserva;
import hotel.repository.ReservaRepository;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class ReservaService {

    private ReservaRepository repository;
    private HabitacionService habitacionService;

    public ReservaService(ReservaRepository repository, HabitacionService habitacionService) {
        this.repository = repository;
        this.habitacionService = habitacionService;
    }

    public Reserva registrarReserva(Reserva reserva) throws Exception {
        //Validar fechas
        if (!reserva.getFechaEntrada().isBefore(reserva.getFechaSalida())) {
            throw new Exception("Fecha de entrada debe ser menor que fecha de salida");
        }

        //Validar duración máxima
        long dias = ChronoUnit.DAYS.between(reserva.getFechaEntrada(), reserva.getFechaSalida());
        if (dias > 30) {
            throw new Exception("No se permiten reservas mayores a 30 días");
        }

        //Validar reservas existentes del cliente
        List<Reserva> reservasCliente = repository.getReservasPorCliente(reserva.getClienteId());
        for (Reserva r : reservasCliente) {
            if (!(reserva.getFechaSalida().isBefore(r.getFechaEntrada()) || reserva.getFechaEntrada().isAfter(r.getFechaSalida()))) {
                throw new Exception("El cliente ya tiene una reserva activa en esas fechas");
            }
        }

        //Validar disponibilidad de habitación
        if (!habitacionService.hayDisponibilidad(reserva.getTipoHabitacion(), reserva.getFechaEntrada(), reserva.getFechaSalida())) {
            throw new Exception("No hay disponibilidad, sugerir alternativas");
        }

        //Generar código de reserva único
        String codigo;
        do {
            codigo = "HR" + (int)(Math.random() * 9000 + 1000);
        } while (repository.existeCodigoReserva(codigo));
        reserva.setCodigoReserva(codigo);

        //Aplicar descuento si la reserva es mayor a 7 días
        if (dias > 7) {
            reserva.setDescuentoAplicado(0.1 * dias); //10% por cada día > 7
        } else {
            reserva.setDescuentoAplicado(0.0);
        }

        // 7. Guardar reserva
        repository.guardarReserva(reserva);

        return reserva;
    }
}
