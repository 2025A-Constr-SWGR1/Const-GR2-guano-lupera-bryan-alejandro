package servlets;

import DAO.CalificacionDAO;
import DAO.UsuarioDAO;
import DAO.UsuarioDAOImpl;
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

@WebServlet(name = "SvIndex", value = "/inicio")
public class SvIndex extends HttpServlet {
    private EntityManagerFactory emf;
    private UsuarioDAO usuarioDAO;
    private CalificacionDAO calificacionDAO;
    private RecomendacionService recomendacionService;

    @Override
    public void init() {
        emf = Persistence.createEntityManagerFactory("UFood_PU");
        usuarioDAO = new UsuarioDAOImpl(emf);
        calificacionDAO = new CalificacionDAO(emf);
        recomendacionService = new RecomendacionService(usuarioDAO, calificacionDAO);

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            if (session != null) {
                Comensal comensal = (Comensal) session.getAttribute("usuario");

                if (comensal != null) {
                    // Obtener todos los restaurantes con sus promedios actualizados
                    List<Restaurante> restaurantes = usuarioDAO.obtenerTodosRestaurantes();
                    restaurantes.forEach(r -> {
                        Double promedio = calificacionDAO.calcularPromedioCalificaciones(r.getId());
                        r.setPuntajePromedio(promedio != null ? promedio : 0.0);
                    });

                    // Obtener recomendaciones ya ordenadas
                    List<Restaurante> recomendados = recomendacionService.obtenerRecomendaciones(comensal);

                    // Debug
                    System.out.println("Restaurantes recomendados ordenados:");
                    recomendados.forEach(r ->
                            System.out.println(r.getNombre() + " - " + r.getPuntajePromedio()));

                    req.setAttribute("restaurantesRecomendados", recomendados);
                    req.setAttribute("restaurantes", restaurantes);
                }
            }

            // Deshabilitar caché
            resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            resp.setHeader("Pragma", "no-cache");
            resp.setDateHeader("Expires", 0);

            req.getRequestDispatcher("/index.jsp").forward(req, resp);
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
