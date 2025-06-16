package servlets;

import DAO.UsuarioDAOImpl;
import entidades.Comensal;
import entidades.Restaurante;
import entidades.Usuario;
import exceptions.ServiceException;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import servicios.AuthServiceImpl;

import java.io.IOException;

@WebServlet(name = "SvAuth", urlPatterns = {"/login", "/registro-restaurante", "/registro-comensal"})
public class SvAuth extends HttpServlet {
    private AuthServiceImpl authService;
    private static EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        try {
            if (emf == null) {
                emf = Persistence.createEntityManagerFactory("UFood_PU");
            }
            UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl(emf);
            this.authService = new AuthServiceImpl(usuarioDAO);
        } catch (Exception e) {
            throw new ServletException("Error al inicializar JPA", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        switch (path) {
            case "/login":
                handleLogin(request, response);
                break;
            case "/registro-restaurante":
                handleRestauranteRegistration(request, response);
                break;
            case "/registro-comensal":
                handleComensalRegistration(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombreUsuario = request.getParameter("nombreUsuario");
        String contrasena = request.getParameter("contrasena");

        try {
            Usuario usuario = authService.login(nombreUsuario, contrasena);
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);

            // Redirección según tipo de usuario
            if (usuario instanceof Comensal) {
                Comensal comensal = (Comensal) usuario;
                session.setAttribute("notificaciones", comensal.getNotificaciones());
                response.sendRedirect("inicio"); // La lógica de recomendaciones ahora está en SvIndex
            } else if ("RESTAURANTE".equals(usuario.getTipoUsuario())) {
                response.sendRedirect("crearRestaurante.jsp");
            }
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }

    private void handleRestauranteRegistration(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Crear instancia de Restaurante en lugar de Usuario
            Restaurante restaurante = new Restaurante();
            restaurante.setNombreUsuario(request.getParameter("nombreUsuario"));
            restaurante.setContrasena(request.getParameter("contrasena"));
            restaurante.setEmail(request.getParameter("email"));
            restaurante.setTipoUsuario("RESTAURANTE");

            authService.registrarUsuarioRestaurante(restaurante);
            response.sendRedirect("login.jsp?registroExitoso=true");
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/registro-restaurante.jsp").forward(request, response);
        }
    }

    private void handleComensalRegistration(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String tipoComidaFavorita = request.getParameter("tipoComidaFavorita");
            if (tipoComidaFavorita == null || tipoComidaFavorita.trim().isEmpty()) {
                tipoComidaFavorita = "General";
            }

            // Crear instancia de Comensal en lugar de Usuario
            Comensal comensal = new Comensal();
            comensal.setNombreUsuario(request.getParameter("nombreUsuario"));
            comensal.setContrasena(request.getParameter("contrasena"));
            comensal.setEmail(request.getParameter("email"));
            comensal.setTipoComidaFavorita(tipoComidaFavorita);
            comensal.setTipoUsuario("COMENSAL");

            authService.registrarComensal(comensal, tipoComidaFavorita);
            response.sendRedirect("login.jsp?registroExitoso=true");
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/registro.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
