package servicios;

import entidades.Notificacion;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class NotificacionServiceParametrizedTest {
    @Parameterized.Parameters
    public static Collection<Object[]> datosDePrueba() {
        return Arrays.asList(new Object[][] {
                // mensaje, leidaInicial, esperadoValido
                { "Notificacion 1", false, true },   // no leída, debe devolver true
                { "Notificacion 2", true, false },   // ya leída, debe devolver false
        });
    }

    private String mensaje;
    private boolean leidaInicial;
    private boolean esperadoValido;

    public NotificacionServiceParametrizedTest(String mensaje, boolean leidaInicial, boolean esperadoValido) {
        this.mensaje = mensaje;
        this.leidaInicial = leidaInicial;
        this.esperadoValido = esperadoValido;
    }

    @Test
    public void given_already_read_notification_when_mark_as_read_then_return_false() {
        Notificacion notificacion = null;
        if (mensaje != null) {
            notificacion = new Notificacion(mensaje);
            notificacion.setLeida(leidaInicial);
        }
        NotificacionService notificacionService = new NotificacionService(null, null);
        boolean resultado = notificacionService.marcarComoLeida(notificacion);
        assertEquals(esperadoValido, resultado);
    }
}
