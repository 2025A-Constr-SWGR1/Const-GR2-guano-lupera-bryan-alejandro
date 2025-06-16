package servicios;

import entidades.Planificacion;
import entidades.Restaurante;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.util.Arrays;
import java.util.Collection;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class PlanificacionRestauranteParametrizedTest {

    @Parameterized.Parameters
    public static Collection<Object[]> datosDePrueba() {
        return Arrays.asList(new Object[][]{
                // nombrePlanificacion, hora, nombreRestaurante, esperadoValido
                {"Almuerzo UTP", "12:30", "La Cevichería", true},
                {"Cena Team", "19:00", null, false},           // Restaurante nulo (inválido)
                {"Desayuno", "08:00", "", false}               // Nombre restaurante vacío (inválido)
        });
    }

    private String nombrePlanificacion;
    private String hora;
    private String nombreRestaurante;
    private boolean esperadoValido;

    public PlanificacionRestauranteParametrizedTest(String nombrePlanificacion, String hora,
                                                    String nombreRestaurante, boolean esperadoValido) {
        this.nombrePlanificacion = nombrePlanificacion;
        this.hora = hora;
        this.nombreRestaurante = nombreRestaurante;
        this.esperadoValido = esperadoValido;
    }

    @Test
    public void testAsociarRestauranteAPlanificacion() {
        // Configuración
        Planificacion planificacion = new Planificacion(hora, nombrePlanificacion);
        Restaurante restaurante = (nombreRestaurante != null && !nombreRestaurante.isEmpty())
                ? new Restaurante() {{ setNombre(nombreRestaurante); }}
                : null;

        // Ejecución y verificación
        if (!esperadoValido) {
            try {
                planificacion.setRestaurante(restaurante);
                fail("Debería haber lanzado IllegalArgumentException");
            } catch (IllegalArgumentException e) {
                assertTrue(e.getMessage().contains("Restaurante no válido"));
            }
        } else {
            planificacion.setRestaurante(restaurante);
            assertEquals(nombreRestaurante, planificacion.getRestaurante().getNombre());
        }
    }
}