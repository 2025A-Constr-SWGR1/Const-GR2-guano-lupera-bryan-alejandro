package DAO;

import entidades.Comensal;
import entidades.Restaurante;
import entidades.Usuario;
import java.util.List;

public interface UsuarioDAO {
    Usuario findByNombreUsuario(String nombreUsuario);
    void save(Usuario usuario);
    void insert(Usuario usuario);
    Usuario findById(Long id);
    Comensal obtenerComensalPorId(Long id);

    // Métodos específicos para restaurantes
    List<Restaurante> obtenerTodosRestaurantes();
    List<Restaurante> buscarRestaurantesPorTipo(String tipoComida);
    List<Restaurante> buscarRestaurantes(String busqueda);
    Double calcularPromedioCalificaciones(Long restauranteId);

    void close();
}