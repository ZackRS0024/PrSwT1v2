package hotel.repository;

import hotel.entity.Reserva;
import java.util.List;

public interface ReservaRepository {
    List<Reserva> getReservasPorCliente(String clienteId);
    void guardarReserva(Reserva reserva);
    boolean existeCodigoReserva(String codigoReserva);
}
