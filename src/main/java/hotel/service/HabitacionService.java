package hotel.service;

import java.time.LocalDate;

public interface HabitacionService {
    boolean hayDisponibilidad(String tipoHabitacion, LocalDate entrada, LocalDate salida);
}
