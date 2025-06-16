package servicios;

import DAO.UsuarioDAOImpl;
import entidades.MenuDelDia;
import entidades.Restaurante;
import entidades.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MenuDelDiaTest {

    private MenuDelDiaService menuDelDiaService;
    private Restaurante restauranteMock;

    @BeforeEach
    void setUp() {

        restauranteMock = new Restaurante();
        restauranteMock.setId(1L);
        restauranteMock.setMenuDelDia(new MenuDelDia("Menú inicial", 0));


        UsuarioDAOImpl usuarioDAOMock = new UsuarioDAOImpl(null) {

            @Override
            public Restaurante findById(Long id) {
                return restauranteMock;
            }

            @Override
            public void save(Usuario usuario) {
                restauranteMock = (Restaurante) usuario; // Guardamos en memoria
            }
        };

        menuDelDiaService = new MenuDelDiaService(usuarioDAOMock);
    }

    @Test
    void givenRestauranteWithMenu_whenSumarVoto_thenVoteCountIncrements() {
        int votosIniciales = restauranteMock.getMenuDelDia().getCantidadVotos();

        menuDelDiaService.sumarVoto(1L);

        int votosFinales = restauranteMock.getMenuDelDia().getCantidadVotos();
        assertEquals(votosIniciales + 1, votosFinales);
    }

    @Test
    void givenDescripcionAndId_whenGuardarMenuDelDia_thenMenuIsAssigned() {
        Restaurante restaurante = new Restaurante();
        restaurante.setId(2L);

        // Cambiamos el restaurante mock para esta prueba
        restauranteMock = restaurante;

        Restaurante actualizado = menuDelDiaService.guardarMenuDelDia("Menú del día test", 2L);

        assertNotNull(actualizado.getMenuDelDia());
        assertEquals("Menú del día test", actualizado.getMenuDelDia().getDescripcion());
    }

    @Test
    void givenRestauranteWithoutVotes_whenSumarVoto_thenVoteIsOne() {
        restauranteMock.getMenuDelDia().setCantidadVotos(0);

        menuDelDiaService.sumarVoto(1L);

        assertEquals(1, restauranteMock.getMenuDelDia().getCantidadVotos());
    }
        @Test
    void givenRestauranteWithMenu_whenGuardarMenuDelDia_thenSobrescribeMenu() {
        restauranteMock.getMenuDelDia().setDescripcion("Antiguo menú");
        Restaurante actualizado = menuDelDiaService.guardarMenuDelDia("Nuevo menú", 1L);

        assertNotNull(actualizado.getMenuDelDia());
        assertEquals("Nuevo menú", actualizado.getMenuDelDia().getDescripcion());
    }

    @Test
    void givenRestauranteWithoutMenu_whenSumarVoto_thenThrowsNullPointerException() {
        restauranteMock.setMenuDelDia(null);

        assertThrows(NullPointerException.class, () -> menuDelDiaService.sumarVoto(1L));
    }

    @Test
    void givenRestauranteWithMenu_whenGuardarMenuDelDia_thenVoteCountResets() {
        restauranteMock.getMenuDelDia().setCantidadVotos(5);
        Restaurante actualizado = menuDelDiaService.guardarMenuDelDia("Menú reiniciado", 1L);

        assertEquals(0, actualizado.getMenuDelDia().getCantidadVotos());
    }
}
