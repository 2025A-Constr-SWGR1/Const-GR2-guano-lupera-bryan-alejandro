package servicios;

import DAO.NotificacionDAO;
import DAO.UsuarioDAO;
import entidades.Comensal;
import entidades.Notificacion;
import entidades.Restaurante;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class NotificacionService {

    private UsuarioDAO usuarioDAO;
    private NotificacionDAO notificacionDAO;
    private EntityManagerFactory emf;

    public NotificacionService(UsuarioDAO usuarioDAO, NotificacionDAO notificacionDAO) {
        if (usuarioDAO == null && notificacionDAO == null) {
            this.emf = null;
        } else {
            this.emf = Persistence.createEntityManagerFactory("UFood_PU");
        }
        this.usuarioDAO = usuarioDAO;
        this.notificacionDAO = notificacionDAO;
    }

    private void notificarComensalMenuDia(Comensal comensal, String nombreRestaurante) {
        comensal.agregarNotificacion(
                "El restaurante " + nombreRestaurante + " ha publicado un nuevo menú del día:");
        usuarioDAO.save(comensal);
    }

    public boolean notificarComensalesMenuDia(Restaurante restaurante) {
        try {
            if (restaurante.getSuscripciones() == null || restaurante.getSuscripciones().isEmpty()) {
                return false;
            }
            restaurante.getSuscripciones().forEach(suscripcion -> {
                Comensal comensal = suscripcion.getComensal();
                notificarComensalMenuDia(comensal, restaurante.getNombre());
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Boolean marcarComoLeida(Notificacion notificacion) {
        if (notificacion != null && !notificacion.isLeida()) {
            notificacion.setLeida(true);
            return true;
        }
        return false;
    }

    public Boolean marcarNotificacionComoLeida(Notificacion notificacion) {
        if (notificacion != null && notificacion.getId() != null) {
            Notificacion notifBD = notificacionDAO.buscarPorId(notificacion.getId());
            if (notifBD != null && !notifBD.isLeida()) {
                if (marcarComoLeida(notifBD)) {
                    notificacionDAO.actualizar(notifBD);
                    return true;
                }
            }
        }
        return false;
    }
}
