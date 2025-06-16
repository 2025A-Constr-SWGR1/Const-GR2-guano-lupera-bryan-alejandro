package servicios;

import entidades.Comensal;
import entidades.Restaurante;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class RecomendacionesServiceTest {

    @Test
    public void testGenerarRecomendacionesParaComensal() {
        // Arrange
        Comensal comensal = new Comensal();
        comensal.setTipoComidaFavorita("Comida Vegetariana");

        List<Restaurante> todosRestaurantes = new ArrayList<>();

        Restaurante r1 = new Restaurante();
        r1.setTipoComida("Comida Vegetariana");
        r1.setPuntajePromedio(4.5);

        Restaurante r2 = new Restaurante();
        r2.setTipoComida("Comida Vegetariana");
        r2.setPuntajePromedio(4.2);

        Restaurante r3 = new Restaurante();
        r3.setTipoComida("Comida Rapida");
        r3.setPuntajePromedio(3.8);

        todosRestaurantes.add(r1);
        todosRestaurantes.add(r2);
        todosRestaurantes.add(r3);

        // Act
        List<Restaurante> recomendados = todosRestaurantes.stream()
            .filter(r -> r.getTipoComida() != null &&
                         r.getTipoComida().equals(comensal.getTipoComidaFavorita()))
            .sorted((a, b) -> {
                Double aScore = a.getPuntajePromedio();
                Double bScore = b.getPuntajePromedio();
                if (aScore == null && bScore == null) return 0;
                if (aScore == null) return 1;
                if (bScore == null) return -1;
                return Double.compare(bScore, aScore);
            })
            .collect(Collectors.toList()); // Cambiado a collect(Collectors.toList())

        // Assert
        assertAll(
            () -> assertEquals(2, recomendados.size(), "Deberían haber 2 restaurantes recomendados"),
            () -> assertEquals("Comida Vegetariana", recomendados.get(0).getTipoComida()),
            () -> assertEquals("Comida Vegetariana", recomendados.get(1).getTipoComida()),
            () -> assertNotNull(recomendados.get(0).getPuntajePromedio(),
                "Puntaje del primer restaurante no debe ser nulo"),
            () -> assertNotNull(recomendados.get(1).getPuntajePromedio(),
                "Puntaje del segundo restaurante no debe ser nulo"),
            () -> assertTrue(recomendados.get(0).getPuntajePromedio() > recomendados.get(1).getPuntajePromedio(),
                "El primer restaurante debería tener mayor puntaje que el segundo")
        );
    }
}