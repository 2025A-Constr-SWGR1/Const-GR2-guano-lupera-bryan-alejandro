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

    @Override
    public void destroy() {
        if (usuarioDAO != null)
            usuarioDAO.close();
        if (emf != null)
            emf.close();
    }
}
