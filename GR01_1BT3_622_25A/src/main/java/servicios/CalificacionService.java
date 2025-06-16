package servicios;

import DAO.CalificacionDAO;
import DAO.UsuarioDAO;
import entidades.Calificacion;
import entidades.Comensal;
import entidades.Restaurante;
import exceptions.ServiceException;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Map;

public class CalificacionService {
    private final CalificacionDAO calificacionDAO;
    private final UsuarioDAO usuarioDAO;

    public CalificacionService(CalificacionDAO calificacionDAO, UsuarioDAO usuarioDAO) {
        this.calificacionDAO = calificacionDAO;
        this.usuarioDAO = usuarioDAO;
    }

    public void crearCalificacion(Calificacion calificacion) throws ServiceException {
        try {
            if (!calificacionDAO.crear(calificacion)) {
                throw new ServiceException("No se pudo crear la calificación");
            }
            actualizarPuntajePromedio(calificacion.getRestaurante());
        } catch (Exception e) {
            throw new ServiceException("Error al crear calificación: " + e.getMessage(), e);
        }
    }

    public List<Calificacion> obtenerCalificacionesPorRestaurante(Long restauranteId) {
        try {
            return calificacionDAO.obtenerCalificacionesPorRestaurante(restauranteId);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener calificaciones", e);
        }
    }

    public void calificar(Map<String, Object> parametrosCalificacion) throws ServiceException {
        try {
            Double puntaje = (Double) parametrosCalificacion.get("puntaje");
            String comentario = (String) parametrosCalificacion.get("comentario");
            Long idComensal = (Long) parametrosCalificacion.get("idComensal");
            Long idRestaurante = (Long) parametrosCalificacion.get("idRestaurante");

            Comensal comensal = usuarioDAO.obtenerComensalPorId(idComensal);
            Restaurante restaurante = (Restaurante) usuarioDAO.findById(idRestaurante);

            if (comensal == null || restaurante == null) {
                throw new ServiceException("Comensal o restaurante no encontrado");
            }

            // Buscar si ya existe una calificación previa del mismo comensal para el mismo
            // restaurante
            Calificacion calificacionExistente = calificacionDAO.obtenerCalificacionPorComensalYRestaurante(idComensal,
                    idRestaurante);

            if (calificacionExistente != null) {

                System.out.println("Calificación id existente: " + calificacionExistente.getId() + " - comentario:"
                        + calificacionExistente.getComentario());
                // Actualizar la calificación existente
                calificacionExistente.setPuntaje(puntaje);
                calificacionExistente.setComentario(comentario);

                if (!calificacionDAO.actualizar(calificacionExistente)) {
                    throw new ServiceException("No se pudo actualizar la calificación existente");
                }

                // Actualizar el promedio del restaurante
                actualizarPuntajePromedio(restaurante);
            } else {
                // Crear una nueva calificación
                Calificacion nuevaCalificacion = new Calificacion();
                nuevaCalificacion.setPuntaje(puntaje);
                nuevaCalificacion.setComentario(comentario);
                nuevaCalificacion.setComensal(comensal);
                nuevaCalificacion.setRestaurante(restaurante);

                crearCalificacion(nuevaCalificacion);
            }
        } catch (Exception e) {
            throw new ServiceException("Error al procesar la calificación: " + e.getMessage(), e);
        }
    }

    private void actualizarPuntajePromedio(Restaurante restaurante) {
        try {
            Double nuevoPromedio = calificacionDAO.calcularPromedioCalificaciones(restaurante.getId());
            System.out.println("Nuevo promedio calculado: " + nuevoPromedio);
            restaurante.setPuntajePromedio(nuevoPromedio);
            usuarioDAO.save(restaurante); // Usar el DAO existente para guardar
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar promedio", e);
        }
    }
}
