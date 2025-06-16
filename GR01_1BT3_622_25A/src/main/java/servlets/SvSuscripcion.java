package servlets;

import DAO.SuscripcionDAO;
import DAO.UsuarioDAO;
import DAO.UsuarioDAOImpl;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servicios.SuscripcionService;

import java.io.IOException;
import java.net.URLEncoder;

@WebServlet(name = "suscribirse", value = "/suscribirse")
public class SvSuscripcion extends HttpServlet {
    private EntityManagerFactory emf;
    private UsuarioDAO usuarioDAO;
    private SuscripcionService suscripcionService;
    private SuscripcionDAO suscripcionDAO;

    @Override
    public void init() {
        emf = Persistence.createEntityManagerFactory("UFood_PU");
        usuarioDAO = new UsuarioDAOImpl(emf);
        suscripcionDAO = new SuscripcionDAO();
        suscripcionService = new SuscripcionService(usuarioDAO, suscripcionDAO);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Long idComensal = Long.parseLong(req.getParameter("idComensal"));
        Long idRestaurante = Long.parseLong(req.getParameter("idRestaurante"));

        try {
            suscripcionService.suscribir(idComensal, idRestaurante);
            resp.sendRedirect(req.getContextPath() + "/inicio?success=suscrito");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/inicio?error=" +
                    URLEncoder.encode("Error al procesar la suscripción: " + e.getMessage(), "UTF-8"));
        }
    }

    private Long obtenerIdComensal(HttpServletRequest req) {
        String idComensalString = req.getParameter("idComensal");
        if (idComensalString == null || idComensalString.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(idComensalString);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    // private void procesarDesuscribirse(HttpServletRequest req,
    // HttpServletResponse resp)
    // throws IOException {
    // try {
    // // Obtener el id del comensal
    // Long idComensal = Long.parseLong(req.getParameter("idComensal"));

    // // Obtener el comensal
    // Comensal comensal = usuarioDAO.obtenerComensalPorId(idComensal);
    // if (comensal == null) {
    // resp.sendRedirect(req.getContextPath() + "/inicio?error=" +
    // URLEncoder.encode("Comensal no encontrado", "UTF-8"));
    // return;
    // }

    // // Obtener el id del restaurante
    // String idRestauranteStr = req.getParameter("idRestaurante");
    // if (idRestauranteStr == null) {
    // resp.sendRedirect(req.getContextPath() + "/inicio?error=" +
    // URLEncoder.encode("Restaurante no especificado", "UTF-8"));
    // return;
    // }
    // Long idRestaurante = Long.parseLong(idRestauranteStr);

    // // Obtener el restaurante
    // Restaurante restaurante = (Restaurante) usuarioDAO.findById(idRestaurante);
    // if (restaurante == null) {
    // resp.sendRedirect(req.getContextPath() + "/inicio?error=" +
    // URLEncoder.encode("Restaurante no encontrado", "UTF-8"));
    // return;
    // }

    // // Desuscribir al comensal
    // comensal.desuscribirseDeRestaurante(restaurante);

    // // Guardar los cambios
    // usuarioDAO.save(comensal);

    // // Actualizar la sesión
    // req.getSession().setAttribute("usuario", comensal);

    // // Redirigir con mensaje de éxito
    // resp.sendRedirect(req.getContextPath() + "/inicio?success=" +
    // URLEncoder.encode("Te has desuscrito exitosamente de " +
    // restaurante.getNombre(), "UTF-8"));
    // } catch (Exception e) {
    // resp.sendRedirect(req.getContextPath() + "/inicio?error=" +
    // URLEncoder.encode("Error al desuscribirse: " + e.getMessage(), "UTF-8"));
    // }
    // }

    @Override
    public void destroy() {
        if (usuarioDAO != null)
            usuarioDAO.close();
        if (emf != null)
            emf.close();
    }
}
