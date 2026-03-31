package hotel.service;

import hotel.entity.Reserva;
import hotel.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ReservaServiceTest {
    @Mock
    private ReservaRepository repository;

    @Mock
    private HabitacionService habitacionService;

    @InjectMocks
    private ReservaService reservaService; // Mockito inyecta repository y habitacionService

    @BeforeAll
    public static void initAll() {
        System.out.println("BeforeAll: inicialización global");
    }

    @BeforeEach
    public void initEach() {
        // Inicializa los mocks antes de cada test
        MockitoAnnotations.openMocks(this);
        System.out.println("BeforeEach: inicialización de mocks y servicio");
    }

    @Test
    void testReservaConFechasInvalidas() {
        Reserva reserva = new Reserva("cliente1", LocalDate.of(2026, 4, 5), LocalDate.of(2026, 4, 1), "Doble");

        Exception ex = assertThrows(Exception.class, () -> reservaService.registrarReserva(reserva));
        assertEquals("Fecha de entrada debe ser menor que fecha de salida", ex.getMessage());
    }

    @Test
    void testReservaMayorA30Dias() {
        Reserva reserva = new Reserva("cliente1", LocalDate.of(2026, 4, 1), LocalDate.of(2026, 5, 5), "Doble");

        Exception ex = assertThrows(Exception.class, () -> reservaService.registrarReserva(reserva));
        assertEquals("No se permiten reservas mayores a 30 días", ex.getMessage());
    }

    @Test
    void testClienteNoPuedeTenerReservasDuplicadas() {
        LocalDate entrada = LocalDate.of(2026, 4, 1);
        LocalDate salida = LocalDate.of(2026, 4, 5);
        Reserva reservaExistente = new Reserva("cliente1", entrada, salida, "Doble");

        when(repository.getReservasPorCliente("cliente1")).thenReturn(List.of(reservaExistente));

        Reserva nuevaReserva = new Reserva("cliente1", entrada.plusDays(1), salida.plusDays(1), "Doble");

        Exception ex = assertThrows(Exception.class, () -> reservaService.registrarReserva(nuevaReserva));
        assertEquals("El cliente ya tiene una reserva activa en esas fechas", ex.getMessage());
    }

    @Test
    void testNoHayDisponibilidadHabitacion() {
        LocalDate entrada = LocalDate.of(2026, 5, 1);
        LocalDate salida = LocalDate.of(2026, 5, 5);
        Reserva reserva = new Reserva("cliente2", entrada, salida, "Suite");

        when(repository.getReservasPorCliente("cliente2")).thenReturn(List.of());
        when(habitacionService.hayDisponibilidad("Suite", entrada, salida)).thenReturn(false);

        Exception ex = assertThrows(Exception.class, () -> reservaService.registrarReserva(reserva));
        assertEquals("No hay disponibilidad, sugerir alternativas", ex.getMessage());
    }

    @Test
    void testReservaExitosaConDescuento() throws Exception {
        LocalDate entrada = LocalDate.of(2026, 5, 1);
        LocalDate salida = LocalDate.of(2026, 5, 10); // 9 días → aplica descuento
        Reserva reserva = new Reserva("cliente3", entrada, salida, "Suite");

        when(repository.getReservasPorCliente("cliente3")).thenReturn(List.of());
        when(habitacionService.hayDisponibilidad("Suite", entrada, salida)).thenReturn(true);
        when(repository.existeCodigoReserva(anyString())).thenReturn(false);

        Reserva result = reservaService.registrarReserva(reserva);

        assertNotNull(result.getCodigoReserva());
        assertTrue(result.getDescuentoAplicado() > 0);
        verify(repository, times(1)).guardarReserva(result);
    }

    @Test
    void testReservaExitosaSinDescuento() throws Exception {
        LocalDate entrada = LocalDate.of(2026, 5, 1);
        LocalDate salida = LocalDate.of(2026, 5, 5); // 4 días → no aplica descuento
        Reserva reserva = new Reserva("cliente4", entrada, salida, "Simple");

        when(repository.getReservasPorCliente("cliente4")).thenReturn(List.of());
        when(habitacionService.hayDisponibilidad("Simple", entrada, salida)).thenReturn(true);
        when(repository.existeCodigoReserva(anyString())).thenReturn(false);

        Reserva result = reservaService.registrarReserva(reserva);

        assertNotNull(result.getCodigoReserva());
        assertEquals(0.0, result.getDescuentoAplicado());
        verify(repository, times(1)).guardarReserva(result);
    }
}