package servicios;

import DAO.PlanificacionDAO;
import entidades.Restaurante;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class PlanificacionSugerirParametrizedTest {
    private final Double puntajePromedio;
    private final Double distanciaUniversidad;
    private final int tiempoEspera;
    private final boolean esperadoRecomendado;

    public PlanificacionSugerirParametrizedTest(Double puntajePromedio, Double distanciaUniversidad, int tiempoEspera, boolean esperadoRecomendado) {
        this.puntajePromedio = puntajePromedio;
        this.distanciaUniversidad = distanciaUniversidad;
        this.tiempoEspera = tiempoEspera;
        this.esperadoRecomendado = esperadoRecomendado;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> datosDePrueba() {
        return Arrays.asList(new Object[][]{
                {4.5, 1.0, 15, true},
                {3.8, 2.5, 20, true},

                {2.0, 1.0, 10, false},
                {4.0, 10.0, 15, false},
                {4.2, 1.5, 40, false}
        });
    }

    @Test
    public void sugerirRestaurante() {
        Restaurante restaurante = new Restaurante();
        restaurante.setPuntajePromedio(puntajePromedio);
        restaurante.setDistanciaUniversidad(distanciaUniversidad);
        restaurante.setTiempoEspera(tiempoEspera);

        PlanificacionDAO planificacionDAONull = null;
        PlanificacionService planificacion = new PlanificacionService(planificacionDAONull);
        boolean esRecomendado = planificacion.recomendarRestaurante(restaurante);

        assertEquals(esperadoRecomendado, esRecomendado);
    }
}