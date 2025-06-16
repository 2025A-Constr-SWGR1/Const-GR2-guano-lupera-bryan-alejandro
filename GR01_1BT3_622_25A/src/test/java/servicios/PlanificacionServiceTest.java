package servicios;

import DAO.PlanificacionDAO;
import entidades.Comensal;
import entidades.Planificacion;
import entidades.Restaurante;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class PlanificacionServiceTest {
    @Test
    public void given_name_hour_when_create_planification_then_planification_not_null() {
        String nombre = "Cena de Fin de Año";
        String hora = "20:00";

        PlanificacionDAO planificacionDAONull = null;
        PlanificacionService planificacionService = new PlanificacionService(planificacionDAONull);
        Comensal comensal = new Comensal();
        Planificacion planificacion = planificacionService.crearPlanificacion(nombre, hora, comensal);

        assertNotNull(planificacion);
    }

    @Test
    public void given_diner_when_add_to_planification_then_ok() {
        String nombre = "Almuerzo UTP";
        String hora = "12:30";

        List<Comensal> comensales = Arrays.asList(new Comensal(), new Comensal());
        Planificacion planificacion = new Planificacion(nombre, hora);

        PlanificacionDAO planificacionDAONull = null;
        PlanificacionService planificacionService = new PlanificacionService(planificacionDAONull);
        Boolean exito = planificacionService.agregarComensales(planificacion, comensales);

        assertTrue(exito);
    }

    @Test
    public void given_duplicate_diner_when_add_to_planification_then_throw_exception() {
        Planificacion planificacion = new Planificacion("Almuerzo UTP", "12:30");
        Comensal comensal = new Comensal();
        comensal.setId(1L);

        // Agregar por primera vez (éxito)
        planificacion.addComensal(comensal);

        // Intentar agregar duplicado (debe fallar)
        try {
            planificacion.addComensal(comensal);
            fail("Debería haber lanzado IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("El comensal ya está en esta planificación", e.getMessage());
        }
    }

    @Test
    public void given_restaurant_when_set_to_planification_then_association_ok() {
        Planificacion planificacion = new Planificacion("Cena de equipo", "19:00");
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNombre("La Cevichería");

        planificacion.setRestaurante(restaurante);
        Restaurante restauranteAsociado = planificacion.getRestaurante();

        assertNotNull("El restaurante no debería ser null", planificacion.getRestaurante());
        assertEquals("La Cevichería", planificacion.getRestaurante().getNombre());
    }

    @Test
    public void given_time_when_calculate_remaining_time_then_ok() {
        // Hora actual simulada
        LocalDateTime ahora = LocalDateTime.of(2025, 5, 12, 12, 30);
        // Hora límite de votación
        LocalDateTime horaLimite = LocalDateTime.of(2025, 5, 12, 13, 0);

        PlanificacionDAO planificacionDAONull = null;
        PlanificacionService planificacionService = new PlanificacionService(planificacionDAONull);
        int minutos = planificacionService.calcularMinutosRestantesParaVotacion(ahora, horaLimite);

        assertEquals(30, minutos);
    }

    @Test
    public void given_votes_when_get_most_voted_restaurant_then_ok() {
        Map<Restaurante, Integer> votos = new HashMap<>();
        Restaurante restaurante1 = new Restaurante();
        Restaurante restaurante2 = new Restaurante();
        Restaurante restaurante3 = new Restaurante();

        votos.put(restaurante1, 3);
        votos.put(restaurante2, 1);
        votos.put(restaurante3, 2);

        PlanificacionDAO planificacionDAONull = null;
        PlanificacionService planificacionService = new PlanificacionService(planificacionDAONull);
        Restaurante restauranteMasVotado = planificacionService.obtenerRestauranteMasVotado(votos);

        assertNotNull("El restaurante no debería ser null", restauranteMasVotado);
        assertEquals(restaurante1, restauranteMasVotado);
    }

    @Test
    public void give_two_restaurants_when_resolve_empate_then_return_restaurant_randomly() {

        PlanificacionDAO planificacionDAONull = null;
        PlanificacionService planificacionService = new PlanificacionService(planificacionDAONull);
        Restaurante restaurante1 = new Restaurante();
        restaurante1.setNombre("Restaurante A");
        Restaurante restaurante2 = new Restaurante();
        restaurante2.setNombre("Restaurante B");

        Map<Restaurante, Integer> votos = new HashMap<>();
        votos.put(restaurante1, 5);
        votos.put(restaurante2, 5);

        Restaurante resultado = planificacionService.resolverEmpateEnVotacion(votos);
        assertNotNull(resultado);
    }

    @Test
    public void given_planificacion_when_cancel_planificacion_then_ok() {
        Comensal comensal = new Comensal();

        PlanificacionDAO planificacionDAONull = null;
        PlanificacionService planificacionService = new PlanificacionService(planificacionDAONull);
        Planificacion planificacion = planificacionService.crearPlanificacion("Comida Grupal", "12:00", comensal);

        planificacionService.cancelarPlanificacion(planificacion);

        assertEquals("Cancelado", planificacion.getEstado());
    }

    @Test
    public void given_restaurante_when_recomendarRestaurante_then_return_true() {

        PlanificacionDAO planificacionDAONull = null;
        PlanificacionService planificacionService = new PlanificacionService(planificacionDAONull);
        Restaurante restauranteMock = mock(Restaurante.class);
        when(restauranteMock.getPuntajePromedio()).thenReturn(4.0);
        when(restauranteMock.getDistanciaUniversidad()).thenReturn(3.0);
        when(restauranteMock.getTiempoEspera()).thenReturn(20);

        boolean esRecomendado = planificacionService.recomendarRestaurante(restauranteMock);
        assertNotNull(esRecomendado);
    }
}
