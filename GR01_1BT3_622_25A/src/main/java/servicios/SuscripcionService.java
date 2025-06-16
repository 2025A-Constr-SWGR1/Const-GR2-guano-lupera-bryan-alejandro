package servicios;

import DAO.UsuarioDAO;
import DAO.SuscripcionDAO;
import entidades.Comensal;
import entidades.Restaurante;
import entidades.Suscripcion;

public class SuscripcionService {
    private final UsuarioDAO usuarioDAO;
    private final SuscripcionDAO suscripcionDAO;

    public SuscripcionService(UsuarioDAO usuarioDAO, SuscripcionDAO suscripcionDAO) {
        this.usuarioDAO = usuarioDAO;
        this.suscripcionDAO = suscripcionDAO;
    }

    public void suscribir(Long idComensal, Long idRestaurante) {
        Restaurante restaurante = (Restaurante) usuarioDAO.findById(idRestaurante);
        Comensal comensal = usuarioDAO.obtenerComensalPorId(idComensal);

        Suscripcion suscripcion = new Suscripcion();
        suscripcion.setComensal(comensal);
        suscripcion.setRestaurante(restaurante);
        suscripcionDAO.crear(suscripcion);
    }

    public void desuscribir(Long idComensal, Long idRestaurante) {
        suscripcionDAO.eliminarSuscripcion(idComensal, idRestaurante);
    }

}
