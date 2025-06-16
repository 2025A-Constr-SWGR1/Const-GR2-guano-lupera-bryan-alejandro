package servicios;

import entidades.Comensal;
import entidades.Restaurante;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class RecomendacionServiceParametrizedTest {

    static Stream<Arguments> datosPrueba() {
        return Stream.of(
            Arguments.of(
                "Italiana", // Tipo comida favorita
                Arrays.asList( // Lista de restaurantes de prueba
                    crearRestaurante("Pasta", "Italiana", 4.5),
                    crearRestaurante("Pizza", "Italiana", 4.8),
                    crearRestaurante("Tacos", "Mexicana", 4.2)),
                Arrays.asList("Pizza", "Pasta") // Resultado esperado (nombres en orden)
            )
            // Agrega más casos aquí...
        );
    }

    @ParameterizedTest
    @MethodSource("datosPrueba")
    public void testObtenerRecomendaciones(String tipoComida,
                                         List<Restaurante> restaurantes,
                                         List<String> nombresEsperados) {
        RecomendacionService servicio = new RecomendacionService(null, null);
        Comensal comensal = new Comensal();
        comensal.setTipoComidaFavorita(tipoComida);

        List<Restaurante> resultado = servicio.obtenerRecomendaciones(comensal, restaurantes);

        // Verificaciones
        assertAll(
            () -> assertEquals(nombresEsperados.size(), resultado.size()),
            () -> {
                for (int i = 0; i < nombresEsperados.size(); i++) {
                    assertEquals(nombresEsperados.get(i), resultado.get(i).getNombre());
                    if (i > 0) {
                        assertTrue(resultado.get(i - 1).getPuntajePromedio() >= resultado.get(i).getPuntajePromedio(),
                            "Los restaurantes deben estar ordenados por puntaje descendente");
                    }
                }
            }
        );
    }

    private static Restaurante crearRestaurante(String nombre, String tipoComida, double puntaje) {
        Restaurante r = new Restaurante();
        r.setId((long) nombre.hashCode());
        r.setNombre(nombre);
        r.setTipoComida(tipoComida);
        r.setPuntajePromedio(puntaje);
        return r;
    }
}