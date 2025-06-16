package servicios;

import DAO.UsuarioDAO;
import entidades.Restaurante;
import java.util.List;

public class RestauranteService {
    private final UsuarioDAO usuarioDAO;

    public RestauranteService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public void guardarRestaurante(Restaurante restaurante) {
        usuarioDAO.save(restaurante);
    }

    public Restaurante obtenerRestaurantePorId(Long id) {
        return (Restaurante) usuarioDAO.findById(id);
    }

    public List<Restaurante> obtenerTodosRestaurantes() {
        return usuarioDAO.obtenerTodosRestaurantes();
    }

    public List<Restaurante> buscarRestaurantes(String busqueda) {
        return usuarioDAO.buscarRestaurantes(busqueda);
    }

    public void actualizarRestaurante(Restaurante restaurante) {
        usuarioDAO.save(restaurante);
    }
}