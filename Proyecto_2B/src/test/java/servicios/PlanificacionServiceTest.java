package servicios;

import entidades.Comensal;
import entidades.Planificacion;
import entidades.Restaurante;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlanificacionServiceTest {

    private PlanificacionService planificacionService;

    @BeforeEach
    void setUp() {
        planificacionService = new PlanificacionService(null);
    }

    @Test
    void givenNameAndHour_whenCreatePlanification_thenPlanificationNotNull() {
        String nombre = "Cena de Fin de Año";
        String hora = "20:00";
        Comensal comensal = new Comensal();

        Planificacion planificacion = planificacionService.crearPlanificacion(nombre, hora, comensal);

        assertNotNull(planificacion, "La planificación no debería ser nula");
        assertEquals(nombre, planificacion.getNombre(), "El nombre no coincide");
        assertEquals(hora, planificacion.getHora(), "La hora no coincide");
    }

    @Test
    void givenDiners_whenAddToPlanification_thenSuccess() {
        Planificacion planificacion = new Planificacion("Almuerzo UTP", "12:30");
        List<Comensal> comensales = Arrays.asList(new Comensal(), new Comensal());

        boolean exito = planificacionService.agregarComensales(planificacion, comensales);

        assertTrue(exito, "Debería retornar true al agregar comensales");
        assertEquals(2, planificacion.getComensales().size(), "Debería tener 2 comensales");
    }

    @Test
    void givenDuplicateDiner_whenAddToPlanification_thenThrowException() {
        Planificacion planificacion = new Planificacion("Almuerzo UTP", "12:30");
        Comensal comensal = new Comensal();
        comensal.setId(1L);
        planificacion.addComensal(comensal);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> planificacion.addComensal(comensal),
                "Debería lanzar IllegalArgumentException");

        assertEquals("El comensal ya está en esta planificación", exception.getMessage());
    }

    @Test
    void givenRestaurant_whenSetToPlanification_thenAssociationOk() {
        Planificacion planificacion = new Planificacion("Cena de equipo", "19:00");
        Restaurante restaurante = new Restaurante();
        restaurante.setId(1L);
        restaurante.setNombre("La Cevichería");

        planificacion.addRestaurante(restaurante);

        assertAll("Verificación de asociación de restaurante",
                () -> assertFalse(planificacion.getRestaurantes().isEmpty(),
                        "La lista de restaurantes no debería estar vacía"),
                () -> assertEquals("La Cevichería",
                        planificacion.getRestaurantes().get(0).getNombre(),
                        "El nombre del restaurante no coincide"));
    }

    @Test
    void givenVotes_whenGetMostVotedRestaurant_thenOk() {
        Map<Restaurante, Integer> votos = new HashMap<>();
        Restaurante restaurante1 = new Restaurante();
        Restaurante restaurante2 = new Restaurante();

        votos.put(restaurante1, 3);
        votos.put(restaurante2, 1);

        Restaurante restauranteMasVotado = planificacionService.obtenerRestauranteMasVotado(votos);

        assertAll("Verificación de restaurante más votado",
                () -> assertNotNull(restauranteMasVotado, "El restaurante no debería ser null"),
                () -> assertEquals(restaurante1, restauranteMasVotado,
                        "El restaurante más votado no coincide"));
    }

    @Test
    void givenTie_whenResolveTie_thenReturnRandomRestaurant() {
        Restaurante restaurante1 = new Restaurante();
        Restaurante restaurante2 = new Restaurante();

        Map<Restaurante, Integer> votos = new HashMap<>();
        votos.put(restaurante1, 5);
        votos.put(restaurante2, 5);

        Restaurante resultado = planificacionService.resolverEmpateEnVotacion(votos);

        assertNotNull(resultado, "El resultado no debería ser null");
        assertTrue(votos.containsKey(resultado),
                "El restaurante devuelto debe estar en la lista de votos");
    }

    @Test
    void givenPlanification_whenCancel_thenStatusChanged() {
        Comensal comensal = new Comensal();
        Planificacion planificacion = planificacionService.crearPlanificacion("Comida Grupal", "12:00", comensal);

        planificacionService.cancelarPlanificacion(planificacion);

        assertEquals("Cancelado", planificacion.getEstado(),
                "El estado debería ser 'Cancelado'");
    }

    @Test
    void givenNullName_whenCreatePlanification_thenThrowException() {
        String nombre = null;
        String hora = "20:00";
        Comensal comensal = new Comensal();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> planificacionService.crearPlanificacion(nombre, hora, comensal),
                "Debería lanzar IllegalArgumentException para nombre nulo");

        assertEquals("El nombre es requerido", exception.getMessage());
    }

    @Test
    void givenEmptyName_whenCreatePlanification_thenThrowException() {
        String nombre = "   ";
        String hora = "20:00";
        Comensal comensal = new Comensal();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> planificacionService.crearPlanificacion(nombre, hora, comensal),
                "Debería lanzar IllegalArgumentException para nombre vacío");

        assertEquals("El nombre es requerido", exception.getMessage());
    }

    @Test
    void givenValidParametersWithLimit_whenCreatePlanification_thenSuccess() {
        String nombre = "Almuerzo con límite";
        String hora = "12:00";
        Integer limiteComensales = 5;
        Comensal comensal = new Comensal();

        Planificacion planificacion = planificacionService.crearPlanificacion(nombre, hora, limiteComensales, comensal);

        assertAll("Verificación de planificación con límite",
                () -> assertNotNull(planificacion, "La planificación no debería ser nula"),
                () -> assertEquals(nombre, planificacion.getNombre(), "El nombre no coincide"),
                () -> assertEquals(hora, planificacion.getHora(), "La hora no coincide"),
                () -> assertEquals(limiteComensales, planificacion.getLimiteComensales(), "El límite no coincide"),
                () -> assertEquals("Activa", planificacion.getEstado(), "El estado debería ser Activa"));
    }

    @Test
    void givenZeroLimit_whenCreatePlanificationWithLimit_thenThrowException() {
        String nombre = "Almuerzo con límite";
        String hora = "12:00";
        Integer limiteComensales = 0;
        Comensal comensal = new Comensal();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> planificacionService.crearPlanificacion(nombre, hora, limiteComensales, comensal),
                "Debería lanzar IllegalArgumentException para límite 0");

        assertEquals("El límite de comensales debe ser mayor a 0", exception.getMessage());
    }

    @Test
    void givenNegativeLimit_whenCreatePlanificationWithLimit_thenThrowException() {
        String nombre = "Almuerzo con límite";
        String hora = "12:00";
        Integer limiteComensales = -1;
        Comensal comensal = new Comensal();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> planificacionService.crearPlanificacion(nombre, hora, limiteComensales, comensal),
                "Debería lanzar IllegalArgumentException para límite negativo");

        assertEquals("El límite de comensales debe ser mayor a 0", exception.getMessage());
    }

    @Test
    void givenNullPlanification_whenCancel_thenThrowException() {
        Planificacion planificacion = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> planificacionService.cancelarPlanificacion(planificacion),
                "Debería lanzar IllegalArgumentException para planificación nula");

        assertEquals("La planificación no puede ser nula", exception.getMessage());
    }

    @Test
    void givenNullComensalInList_whenAddToPlanification_thenIgnoreNull() {
        Planificacion planificacion = new Planificacion("Test", "12:30");
        Comensal comensal1 = new Comensal();
        comensal1.setId(1L);
        List<Comensal> comensales = Arrays.asList(comensal1, null, new Comensal());

        boolean exito = planificacionService.agregarComensales(planificacion, comensales);

        assertTrue(exito, "Debería retornar true");
        assertEquals(2, planificacion.getComensales().size(), "Debería agregar solo los comensales no nulos");
    }

    @Test
    void givenEmptyVotes_whenGetMostVoted_thenReturnNull() {
        Map<Restaurante, Integer> votos = new HashMap<>();

        Restaurante resultado = planificacionService.obtenerRestauranteMasVotado(votos);

        assertNull(resultado, "Debería retornar null para mapa vacío");
    }

    @Test
    void givenMultipleRestaurantsWithDifferentVotes_whenResolveTie_thenReturnMostVoted() {
        Map<Restaurante, Integer> votos = new HashMap<>();
        Restaurante restaurante1 = new Restaurante();
        Restaurante restaurante2 = new Restaurante();
        Restaurante restaurante3 = new Restaurante();

        votos.put(restaurante1, 2);
        votos.put(restaurante2, 5);
        votos.put(restaurante3, 1);

        Restaurante resultado = planificacionService.resolverEmpateEnVotacion(votos);

        assertEquals(restaurante2, resultado, "Debería retornar el restaurante con más votos");
    }
}
