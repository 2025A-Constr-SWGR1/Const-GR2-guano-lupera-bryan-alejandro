package servicios;

import entidades.Notificacion;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotificacionServiceParametrizedTest {
    @ParameterizedTest
    @MethodSource("datosDePrueba")
    void testMarcarComoLeida(String mensaje, boolean leidaInicial, boolean esperadoValido) {
        // Crear notificación con estado inicial
        Notificacion notificacion = new Notificacion(mensaje);
        notificacion.setLeida(leidaInicial);

        // Ejecutar el método a probar
        NotificacionService servicio = new NotificacionService(null, null);
        boolean resultado = servicio.marcarComoLeida(notificacion);

        // Verificar el resultado
        assertEquals(esperadoValido, resultado);
    }

    private static Stream<Arguments> datosDePrueba() {
        return Stream.of(
                // mensaje,        leidaInicial,  esperadoValido
                Arguments.of("Notificacion 1", false,         true),
                Arguments.of("Notificacion 2", true,          false)
        );
    }
}
