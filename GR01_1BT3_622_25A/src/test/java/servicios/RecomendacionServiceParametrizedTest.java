package servicios;

import entidades.Comensal;
import entidades.Restaurante;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class RecomendacionServiceParametrizedTest {

    @Parameterized.Parameters
    public static Collection<Object[]> datosPrueba() {
        return Arrays.asList(new Object[][]{
                {
                        "Italiana", // Tipo comida favorita
                        Arrays.asList( // Lista de restaurantes de prueba
                                crearRestaurante("Pasta", "Italiana", 4.5),
                                crearRestaurante("Pizza", "Italiana", 4.8),
                                crearRestaurante("Tacos", "Mexicana", 4.2)),
                        Arrays.asList("Pizza", "Pasta") // Resultado esperado (nombres en orden)
                }
                // Agrega más casos aquí...
        });
    }

    private final String tipoComida;
    private final List<Restaurante> restaurantes;
    private final List<String> nombresEsperados;

    public RecomendacionServiceParametrizedTest(String tipoComida,
                                                List<Restaurante> restaurantes,
                                                List<String> nombresEsperados) {
        this.tipoComida = tipoComida;
        this.restaurantes = restaurantes;
        this.nombresEsperados = nombresEsperados;
    }

    @Test
    public void testObtenerRecomendaciones() {
        RecomendacionService servicio = new RecomendacionService(null, null);
        Comensal comensal = new Comensal();
            comensal.setTipoComidaFavorita(tipoComida);

        // Llama al método modificado que acepta la lista directamente
        List<Restaurante> resultado = servicio.obtenerRecomendaciones(comensal, restaurantes);

        // Verificaciones
        assertEquals(nombresEsperados.size(), resultado.size());
        for (int i = 0; i < nombresEsperados.size(); i++) {
            assertEquals(nombresEsperados.get(i), resultado.get(i).getNombre());
            if (i > 0) {
                assertTrue(resultado.get(i - 1).getPuntajePromedio() >= resultado.get(i).getPuntajePromedio());
            }
        }
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