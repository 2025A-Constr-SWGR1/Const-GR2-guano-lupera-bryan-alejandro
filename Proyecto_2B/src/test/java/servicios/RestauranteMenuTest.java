package servicios;

import entidades.Menu;
import entidades.Restaurante;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RestauranteMenuTestActual {
    private Restaurante restaurante;
    private Menu menu1;
    private Menu menu2;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante("Test Restaurant", "Comida Rápida");
        menu1 = new Menu("Menú del día");
        menu2 = new Menu("Menú ejecutivo");
    }

    @Test
    void testAgregarMenu_EstableceRelacionBidireccional() {
        restaurante.agregarMenu(menu1);

        // Verifica que el menú fue agregado y la relación se estableció
        assertTrue(restaurante.getMenus().contains(menu1));
        assertEquals(restaurante, menu1.getRestaurante());
    }

    @Test
    void testAgregarMenu_NoRemueveAutomaticamenteDeRestauranteAnterior() {
        Restaurante otroRestaurante = new Restaurante("Otro", "Comida China");
        otroRestaurante.agregarMenu(menu1);

        restaurante.agregarMenu(menu1);

        // Comportamiento actual: El menú sigue en ambos restaurantes
        assertTrue(otroRestaurante.getMenus().contains(menu1));
        assertTrue(restaurante.getMenus().contains(menu1));
        assertEquals(restaurante, menu1.getRestaurante()); // Sobrescribe la relación
    }

    @Test
    void testAgregarMenu_PermiteDuplicados() {
        restaurante.agregarMenu(menu1);
        restaurante.agregarMenu(menu1);

        assertEquals(2, restaurante.getMenus().size()); // Comportamiento actual
    }

    @Test
    void testSetMenus_NoEstableceRelacionBidireccionalAutomaticamente() {
        List<Menu> menus = Arrays.asList(menu1, menu2);

        restaurante.setMenus(menus);

        // Comportamiento actual: setMenus no establece menu.setRestaurante(this)
        assertEquals(2, restaurante.getMenus().size());
        assertNull(menu1.getRestaurante()); // No se estableció la relación
        assertNull(menu2.getRestaurante());
    }

    @Test
    void testGetMenus_InicializaListaSiEsNull() {
        restaurante.setMenus(null);
        assertNotNull(restaurante.getMenus());
        assertTrue(restaurante.getMenus().isEmpty());
    }
}