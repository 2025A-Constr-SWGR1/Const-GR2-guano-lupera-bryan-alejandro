package servlets;

import DAO.RestauranteDAO;
import DAO.UsuarioDAO;
import entidades.DuenioRestaurante;
import entidades.Menu;
import entidades.Restaurante;
import entidades.Usuario;
import servicios.MenuDelDiaService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalTime;

@WebServlet(name = "SvRestaurante", value = "/restaurante")
public class SvRestaurante extends HttpServlet {
    private EntityManagerFactory emf;
    private UsuarioDAO usuarioDAO;
    private RestauranteDAO restauranteDAO;

    @Override
    public void init() throws ServletException {
        emf = Persistence.createEntityManagerFactory("UFood_PU");
        usuarioDAO = new UsuarioDAO(emf);
        restauranteDAO = new RestauranteDAO(emf);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        // Validar sesión
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Redirigir según tipo de usuario
        if ("DUENO_RESTAURANTE".equals(usuario.getTipoUsuario())) {
            DuenioRestaurante duenio = (DuenioRestaurante) usuario;
            mostrarPanelRestaurante(req, resp, duenio.getRestaurante());
        } else {
            resp.sendRedirect(req.getContextPath() + "/inicio");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("usuario") == null ||
                !"DUENO_RESTAURANTE".equals(((Usuario) session.getAttribute("usuario")).getTipoUsuario())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        DuenioRestaurante duenio = (DuenioRestaurante) session.getAttribute("usuario");
        Restaurante restauranteUsuario = duenio.getRestaurante();
        String accion = req.getParameter("accion");

        try {
            switch (accion) {
                case "guardar":
                    procesarGuardarRestaurante(req, resp, restauranteUsuario);
                    break;
                case "agregarMenu":
                    Menu menu = new Menu(req.getParameter("menu"));
                    procesarAgregarMenu(req, resp, restauranteUsuario, menu);
                    break;
                case "actualizar":
                    procesarActualizarRestaurante(req, resp, restauranteUsuario);
                    break;
                case "agregarMenuDelDia":
                    String descripcionMenu = req.getParameter("historia");
                    System.out.println(restauranteUsuario.getId());
                    if (descripcionMenu != null && !descripcionMenu.trim().isEmpty()) {
                        MenuDelDiaService menuDelDiaService = new MenuDelDiaService();
                        Restaurante restaurante = menuDelDiaService.guardarMenuDelDia(descripcionMenu,
                                restauranteUsuario.getId());
                        duenio.setRestaurante(restaurante);
                        usuarioDAO.save(duenio);
                        session.setAttribute("usuario", duenio);
                    }
                    break;
                default:
                    resp.sendRedirect(req.getContextPath() + "/crearRestaurante.jsp");
            }
        } catch (Exception e) {
            resp.sendRedirect(
                    req.getContextPath() + "/restaurante?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

    private void mostrarPanelRestaurante(HttpServletRequest req, HttpServletResponse resp,
            Restaurante restauranteUsuario) throws ServletException, IOException {
        try {
            // Manejo de mensajes
            String success = req.getParameter("success");
            String error = req.getParameter("error");

            if (success != null)
                req.setAttribute("successMessage", success);
            if (error != null)
                req.setAttribute("errorMessage", error);

            // Obtener datos actualizados del restaurante
            Restaurante restaurante = restauranteDAO.obtenerRestaurantePorId(restauranteUsuario.getId());
            req.setAttribute("restaurante", restaurante);

            req.getRequestDispatcher("crearRestaurante.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Error al cargar datos: " + e.getMessage());
            req.getRequestDispatcher("crearRestaurante.jsp").forward(req, resp);
        }
    }

    private void procesarGuardarRestaurante(HttpServletRequest req, HttpServletResponse resp,
            Restaurante restauranteUsuario) throws IOException {
        try {
            // Actualizar propiedades básicas
            restauranteUsuario.setNombre(req.getParameter("nombre"));
            restauranteUsuario.setDescripcion(req.getParameter("descripcion"));
            restauranteUsuario.setTipoComida(req.getParameter("tipoComida"));
            restauranteUsuario.setHoraApertura(LocalTime.parse(req.getParameter("horaApertura")));
            restauranteUsuario.setHoraCierre(LocalTime.parse(req.getParameter("horaCierre")));

            // Actualizar propiedades numéricas con validación
            try {
                restauranteUsuario.setTiempoEspera(Integer.parseInt(req.getParameter("tiempoEspera")));
                restauranteUsuario.setCalidad(Integer.parseInt(req.getParameter("calidad")));
                restauranteUsuario.setPrecio(Integer.parseInt(req.getParameter("precio")));
                restauranteUsuario
                        .setDistanciaUniversidad(Double.parseDouble(req.getParameter("distanciaUniversidad")));
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/restaurante?error=Formato+numérico+inválido");
                return;
            }

            // Guardar cambios
            restauranteDAO.save(restauranteUsuario);

            // Actualizar sesión
            HttpSession session = req.getSession();
            DuenioRestaurante duenio = (DuenioRestaurante) session.getAttribute("usuario");
            duenio.setRestaurante(restauranteUsuario);
            session.setAttribute("usuario", duenio);

            resp.sendRedirect(req.getContextPath() + "/restaurante?success=Cambios+guardados+exitosamente");
        } catch (Exception e) {
            resp.sendRedirect(
                    req.getContextPath() + "/restaurante?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

    private void procesarAgregarMenu(HttpServletRequest req, HttpServletResponse resp,
            Restaurante restauranteUsuario, Menu menu) throws IOException {
        try {
            // Validar menú
            if (menu == null) {
                resp.sendRedirect(req.getContextPath() + "/restaurante?error=El+menú+no+puede+estar+vacío");
                return;
            }

            // Agregar menú al restaurante
            Restaurante restaurante = restauranteDAO.obtenerRestaurantePorId(restauranteUsuario.getId());
            restaurante.agregarMenu(menu);
            restauranteDAO.save(restaurante);

            // Actualizar sesión
            req.getSession().setAttribute("usuario", ((DuenioRestaurante) req.getSession().getAttribute("usuario")));

            resp.sendRedirect(req.getContextPath() + "/restaurante?success=Menú+agregado+correctamente");
        } catch (Exception e) {
            resp.sendRedirect(
                    req.getContextPath() + "/restaurante?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

    private void procesarActualizarRestaurante(HttpServletRequest req, HttpServletResponse resp,
            Restaurante restauranteUsuario) throws IOException {
        try {
            // Actualizar solo campos proporcionados
            if (req.getParameter("nombre") != null)
                restauranteUsuario.setNombre(req.getParameter("nombre"));
            if (req.getParameter("descripcion") != null)
                restauranteUsuario.setDescripcion(req.getParameter("descripcion"));
            if (req.getParameter("tipoComida") != null)
                restauranteUsuario.setTipoComida(req.getParameter("tipoComida"));
            if (req.getParameter("horaApertura") != null)
                restauranteUsuario.setHoraApertura(LocalTime.parse(req.getParameter("horaApertura")));
            if (req.getParameter("horaCierre") != null)
                restauranteUsuario.setHoraCierre(LocalTime.parse(req.getParameter("horaCierre")));

            // Actualizar campos numéricos si existen
            try {
                if (req.getParameter("tiempoEspera") != null)
                    restauranteUsuario.setTiempoEspera(Integer.parseInt(req.getParameter("tiempoEspera")));
                if (req.getParameter("calidad") != null)
                    restauranteUsuario.setCalidad(Integer.parseInt(req.getParameter("calidad")));
                if (req.getParameter("precio") != null)
                    restauranteUsuario.setPrecio(Integer.parseInt(req.getParameter("precio")));
                if (req.getParameter("distanciaUniversidad") != null)
                    restauranteUsuario
                            .setDistanciaUniversidad(Double.parseDouble(req.getParameter("distanciaUniversidad")));
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/restaurante?error=Formato+numérico+inválido");
                return;
            }

            // Guardar cambios
            restauranteDAO.save(restauranteUsuario);

            // Actualizar sesión
            HttpSession session = req.getSession();
            DuenioRestaurante duenio = (DuenioRestaurante) session.getAttribute("usuario");
            duenio.setRestaurante(restauranteUsuario);
            session.setAttribute("usuario", duenio);

            resp.sendRedirect(req.getContextPath() + "/restaurante?success=Restaurante+actualizado");
        } catch (Exception e) {
            resp.sendRedirect(
                    req.getContextPath() + "/restaurante?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
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
