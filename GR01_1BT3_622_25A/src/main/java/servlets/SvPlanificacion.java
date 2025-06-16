package servlets;

import entidades.Comensal;
import entidades.Planificacion;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servicios.PlanificacionService;
import DAO.PlanificacionDAO;
import DAO.UsuarioDAO;
import DAO.UsuarioDAOImpl;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "planificar", urlPatterns = { "/planificar" })
public class SvPlanificacion extends HttpServlet {
    private PlanificacionService planificacionService;
    private EntityManagerFactory emf;
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("UFood_PU");
        planificacionService = new PlanificacionService(new PlanificacionDAO());
        usuarioDAO = new UsuarioDAOImpl(emf);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PlanificacionDAO planificacionDAO = new PlanificacionDAO();

        Long idComensalPlanificador = obtenerIdComensalPlanificador(request);

        if (idComensalPlanificador == null) {
            request.setAttribute("mensaje", "Error: No se pudo determinar el comensal planificador.");
            request.getRequestDispatcher("crearPlanificacion.jsp").forward(request, response);
            return;
        }

        List<Planificacion> planificaciones = planificacionDAO.obtenerPlanificacionesPorId(idComensalPlanificador);
        request.setAttribute("planificaciones", planificaciones);
        request.getRequestDispatcher("crearPlanificacion.jsp").forward(request, response);
    }

    private Long obtenerIdComensalPlanificador(HttpServletRequest request) {
        String idParam = request.getParameter("idComensalPlanificador");
        if (idParam != null && !idParam.isEmpty()) {
            try {
                return Long.parseLong(idParam);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        Object usuario = request.getSession().getAttribute("usuario");
        if (usuario != null) {
            try {
                java.lang.reflect.Method getId = usuario.getClass().getMethod("getId");
                Object idObj = getId.invoke(usuario);
                if (idObj != null) {
                    return Long.parseLong(idObj.toString());
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String hora = request.getParameter("hora");
        Long idComensalPlanificador = obtenerIdComensalPlanificador(request);
        String mensaje;

        if (idComensalPlanificador == null) {
            mensaje = "Error: No se pudo determinar el comensal planificador.";
            request.setAttribute("mensaje", mensaje);
            request.getRequestDispatcher("crearPlanificacion.jsp").forward(request, response);
            return;
        }

        try {
            Comensal comensal = usuarioDAO.obtenerComensalPorId(idComensalPlanificador);
            Planificacion planificacion = planificacionService.crearPlanificacion(nombre, hora, comensal);
            planificacionService.guardarPlanificacion(planificacion);
            if (planificacion != null && planificacion.getId() != null) {
                mensaje = "Planificación creada exitosamente.";
            } else {
                mensaje = "No se pudo crear la planificación.";
            }
        } catch (Exception e) {
            mensaje = "Error: " + e.getMessage();
        }

        // Mostrar las planificaciones actualizadas
        PlanificacionDAO planificacionDAO = new PlanificacionDAO();
        List<Planificacion> planificaciones = planificacionDAO.obtenerPlanificacionesPorId(idComensalPlanificador);
        request.setAttribute("planificaciones", planificaciones);
        request.setAttribute("mensaje", mensaje);
        request.getRequestDispatcher("crearPlanificacion.jsp").forward(request, response);
    }
}
