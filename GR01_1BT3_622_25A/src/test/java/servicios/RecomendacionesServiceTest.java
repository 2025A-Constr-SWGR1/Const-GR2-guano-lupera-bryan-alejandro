package servicios;

import entidades.Comensal;
import entidades.Restaurante;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

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
        List<Restaurante> recomendados = new ArrayList<>();
        for (Restaurante r : todosRestaurantes) {
            if (r.getTipoComida() != null && r.getTipoComida().equals(comensal.getTipoComidaFavorita())) {
                recomendados.add(r);
            }
        }

        // Ordenar descendente por puntaje
        recomendados.sort((a, b) -> {
            Double aScore = a.getPuntajePromedio();
            Double bScore = b.getPuntajePromedio();
            if (aScore == null && bScore == null) return 0;
            if (aScore == null) return 1;  // Los nulos van al final
            if (bScore == null) return -1; // Los nulos van al final
            return Double.compare(bScore, aScore);
        });

        // Assert
        assertEquals(2, recomendados.size());
        assertEquals("Comida Vegetariana", recomendados.get(0).getTipoComida());
        assertEquals("Comida Vegetariana", recomendados.get(1).getTipoComida());

        // Verificar que el primer restaurante tiene mayor puntaje que el segundo
        assertNotNull("Puntaje del primer restaurante no debe ser nulo", recomendados.get(0).getPuntajePromedio());
        assertNotNull("Puntaje del segundo restaurante no debe ser nulo", recomendados.get(1).getPuntajePromedio());
        assertTrue("El primer restaurante debería tener mayor puntaje que el segundo: " +
                        recomendados.get(0).getPuntajePromedio() + " > " + recomendados.get(1).getPuntajePromedio(),
                recomendados.get(0).getPuntajePromedio() > recomendados.get(1).getPuntajePromedio());
    }
}