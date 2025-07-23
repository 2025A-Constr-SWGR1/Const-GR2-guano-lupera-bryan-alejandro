package servlets;

import DAO.CalificacionDAO;
import DAO.UsuarioDAO;
import DAO.VotoMenuDelDiaDAO;
import entidades.Comensal;
import entidades.Restaurante;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.RecomendacionService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SvIndex", value = {"/inicio", "/home"})
public class SvIndex extends HttpServlet {
    private EntityManagerFactory emf;
    private UsuarioDAO usuarioDAO;
    private CalificacionDAO calificacionDAO;
    private RecomendacionService recomendacionService;
    private VotoMenuDelDiaDAO votoDAO;

    @Override
    public void init() {
        emf = Persistence.createEntityManagerFactory("UFood_PU");
        usuarioDAO = new UsuarioDAO(emf);
        calificacionDAO = new CalificacionDAO(emf);
        votoDAO = new VotoMenuDelDiaDAO(emf);
        recomendacionService = new RecomendacionService(usuarioDAO, calificacionDAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            boolean isLoggedIn = session != null && session.getAttribute("usuario") != null;

            // Configuración común para ambos casos
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setDateHeader("Expires", 0);

            if (isLoggedIn) {
                // Lógica para usuarios LOGUEADOS (va a index.jsp)
                Comensal comensal = (Comensal) session.getAttribute("usuario");

                // Obtener restaurantes recomendados
                List<Restaurante> recomendados = recomendacionService.obtenerRecomendaciones(comensal);

                // Obtener todos los restaurantes con sus promedios
                List<Restaurante> restaurantes = usuarioDAO.obtenerTodosRestaurantes();
                restaurantes.forEach(r -> {
                    Double promedio = calificacionDAO.calcularPromedioCalificaciones(r.getId());
                    r.setPuntajePromedio(promedio != null ? promedio : 0.0);
                    
                    // Verificar si el usuario ya votó por este restaurante
                    if (r.getMenuDelDia() != null) {
                        boolean yaVoto = votoDAO.buscarVotoPorUsuarioYRestaurante(comensal.getId(), r.getId()) != null;
                        req.setAttribute("usuarioYaVoto_" + r.getId(), yaVoto);
                    }
                });

                req.setAttribute("restaurantesRecomendados", recomendados);
                req.setAttribute("restaurantes", restaurantes);

                // Redirigir a index.jsp (para logueados)
                req.getRequestDispatcher("/index.jsp").forward(req, resp);
            } else {
                // Lógica para usuarios NO LOGUEADOS (va a home.jsp)
                // Solo obtener restaurantes básicos
                List<Restaurante> restaurantes = usuarioDAO.obtenerTodosRestaurantes();
                restaurantes.forEach(r -> {
                    Double promedio = calificacionDAO.calcularPromedioCalificaciones(r.getId());
                    r.setPuntajePromedio(promedio != null ? promedio : 0.0);
                });

                req.setAttribute("restaurantes", restaurantes);

                // Redirigir a home.jsp (para no logueados)
                req.getRequestDispatcher("/home.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al cargar restaurantes");
        }
    }

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
