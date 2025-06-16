package servicios;

import entidades.Comensal;
import entidades.Restaurante;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PreferenciaMatchingTest {

    @Test
    public void testCoincidenciaTipoComida() {
        // Arrange
        Comensal comensal = new Comensal();
        comensal.setTipoComidaFavorita("Comida Italiana");

        Restaurante restaurante1 = new Restaurante();
        restaurante1.setTipoComida("Comida Italiana");

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setTipoComida("Comida Mexicana");

        // Act & Assert
        assertTrue(restaurante1.getTipoComida().equals(comensal.getTipoComidaFavorita()));
        assertFalse(restaurante2.getTipoComida().equals(comensal.getTipoComidaFavorita()));
    }
}