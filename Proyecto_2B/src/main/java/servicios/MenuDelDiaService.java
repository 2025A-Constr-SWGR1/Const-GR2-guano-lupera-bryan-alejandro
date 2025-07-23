package servicios;

import DAO.RestauranteDAO;
import DAO.VotoMenuDelDiaDAO;
import entidades.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MenuDelDiaService {

    private final RestauranteDAO restauranteDAO;
    private final VotoMenuDelDiaDAO votoDAO;

    public MenuDelDiaService() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("UFood_PU");
        this.restauranteDAO = new RestauranteDAO(emf);
        this.votoDAO = new VotoMenuDelDiaDAO(emf);
    }

    public MenuDelDiaService(RestauranteDAO restauranteDAO, VotoMenuDelDiaDAO votoDAO) {
        this.restauranteDAO = restauranteDAO;
        this.votoDAO = votoDAO;
    }

    public Restaurante guardarMenuDelDia(String descripcion, Long idRestaurante) {
        Restaurante restaurante = buscarRestaurante(idRestaurante);
        MenuDelDia menuDelDia = new MenuDelDia(descripcion, 0);
        restaurante.setMenuDelDia(menuDelDia);
        guardarRestaurante(restaurante);
        return restaurante;
    }

    public boolean toggleVoto(Long idRestaurante, Usuario usuario) {
        Restaurante restaurante = buscarRestaurante(idRestaurante);
        VotoMenuDelDia votoExistente = votoDAO.buscarVotoPorUsuarioYRestaurante(usuario.getId(), idRestaurante);
        
        if (votoExistente != null) {
            // Ya votó, quitar voto
            votoDAO.eliminarVoto(votoExistente);
            MenuDelDia menuDelDia = restaurante.getMenuDelDia();
            menuDelDia.setCantidadVotos(menuDelDia.getCantidadVotos() - 1);
            guardarRestaurante(restaurante);
            return false; // Voto removido
        } else {
            // No ha votado, agregar voto
            VotoMenuDelDia nuevoVoto = new VotoMenuDelDia(usuario, restaurante);
            votoDAO.guardarVoto(nuevoVoto);
            MenuDelDia menuDelDia = restaurante.getMenuDelDia();
            menuDelDia.setCantidadVotos(menuDelDia.getCantidadVotos() + 1);
            guardarRestaurante(restaurante);
            return true; // Voto agregado
        }
    }
    
    public boolean usuarioYaVoto(Long idRestaurante, Usuario usuario) {
        return votoDAO.buscarVotoPorUsuarioYRestaurante(usuario.getId(), idRestaurante) != null;
    }

    private Restaurante buscarRestaurante(Long id) {
        return restauranteDAO.obtenerRestaurantePorId(id);
    }

    private void guardarRestaurante(Restaurante restaurante) {
        restauranteDAO.save(restaurante);
    }
}