package servicios;

import DAO.NotificacionDAO;
import DAO.UsuarioDAO;
import entidades.Comensal;
import entidades.Notificacion;
import entidades.Restaurante;

public class NotificacionService {

    private UsuarioDAO usuarioDAO;
    private NotificacionDAO notificacionDAO;

    public NotificacionService(UsuarioDAO usuarioDAO, NotificacionDAO notificacionDAO) {
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
        if (notificacion == null || notificacion.getId() == null) {
            return false;
        }

        Notificacion notifBD = notificacionDAO.buscarPorId(notificacion.getId());
        if (notifBD == null || notifBD.isLeida()) {
            return false;
        }

        if (!marcarComoLeida(notifBD)) {
            return false;
        }

        notificacionDAO.actualizar(notifBD);
        return true;
    }
}
