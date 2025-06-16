package servicios;

import entidades.Restaurante;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class RestauranteSorterTest {

    @Test
    public void testOrdenarRestaurantesPorCalificacionDescendente() {
        // Arrange
        Restaurante r1 = new Restaurante();
        r1.setNombre("Restaurante A");
        r1.setPuntajePromedio(4.2);

        Restaurante r2 = new Restaurante();
        r2.setNombre("Restaurante B");
        r2.setPuntajePromedio(4.8);

        Restaurante r3 = new Restaurante();
        r3.setNombre("Restaurante C");
        r3.setPuntajePromedio(3.9);

        List<Restaurante> restaurantes = Arrays.asList(r1, r2, r3);

        // Act
        restaurantes.sort((a, b) -> Double.compare(b.getPuntajePromedio(), a.getPuntajePromedio()));

        // Assert
        assertEquals("Restaurante B", restaurantes.get(0).getNombre());
        assertEquals("Restaurante A", restaurantes.get(1).getNombre());
        assertEquals("Restaurante C", restaurantes.get(2).getNombre());
        assertTrue(restaurantes.get(0).getPuntajePromedio() > restaurantes.get(1).getPuntajePromedio());
        assertTrue(restaurantes.get(1).getPuntajePromedio() > restaurantes.get(2).getPuntajePromedio());
    }
}