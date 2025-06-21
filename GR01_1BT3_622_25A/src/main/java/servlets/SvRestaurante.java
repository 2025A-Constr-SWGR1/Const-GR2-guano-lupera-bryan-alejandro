package servlets;

import DAO.UsuarioDAO;
import DAO.UsuarioDAOImpl;
import entidades.Comensal;
import entidades.Restaurante;
import entidades.Usuario;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.MenuDelDiaService;
import servicios.NotificacionService;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalTime;

import org.hibernate.engine.jdbc.env.internal.LobCreationLogging_.logger;

@WebServlet(name = "SvRestaurante", value = "/restaurante")
public class SvRestaurante extends HttpServlet {
    private EntityManagerFactory emf;
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        emf = Persistence.createEntityManagerFactory("UFood_PU");
        usuarioDAO = new UsuarioDAOImpl(emf);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        // Validar sesión
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");

        // Redirigir según tipo de usuario
        if ("RESTAURANTE".equals(usuario.getTipoUsuario())) {
            mostrarPanelRestaurante(req, resp, (Restaurante) usuario);
        } else {
            resp.sendRedirect(req.getContextPath() + "/inicio");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("usuario") == null ||
                !"RESTAURANTE".equals(((Usuario) session.getAttribute("usuario")).getTipoUsuario())) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        Restaurante restauranteUsuario = (Restaurante) session.getAttribute("usuario");
        String accion = req.getParameter("accion");

        if ("guardar".equals(accion)) {
            procesarGuardarRestaurante(req, resp, restauranteUsuario);
        } else if ("agregarHistoria".equals(accion)) {
            procesarAgregarMenu(req, resp, restauranteUsuario);
            NotificacionService notificacionService = new NotificacionService(usuarioDAO, null);

            notificacionService.notificarComensalesMenuDia(restauranteUsuario);
        } else if ("actualizar".equals(accion)) {
            procesarActualizarRestaurante(req, resp, restauranteUsuario);
        } else if ("subscribirse".equals(accion)) {
            procesarSubscribirse(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/crearRestaurante.jsp");
        }

        if ("agregarMenuDelDia".equals(accion)) {
            String descripcionMenu = req.getParameter("historia");
            System.out.println(restauranteUsuario.getId());
            if (descripcionMenu != null && !descripcionMenu.trim().isEmpty()) {
                // lógica para reemplazar o guardar el menú del día siguiente
                MenuDelDiaService menuDelDiaService = new MenuDelDiaService();
                Restaurante restaurante = menuDelDiaService.guardarMenuDelDia(descripcionMenu,
                        restauranteUsuario.getId());
                session.setAttribute("usuario", restaurante);

            }

        }

    }

    private void procesarSubscribirse(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long idComensal = Long.parseLong(req.getParameter("idComensal"));
        Comensal comensal = usuarioDAO.obtenerComensalPorId(idComensal);
        if (comensal == null) {
            resp.sendRedirect(req.getContextPath() + "/restaurantesFiltrados.jsp?error=Comensal+no+encontrado");
            return;
        }
        String idRestauranteStr = req.getParameter("idRestaurante");
        if (idRestauranteStr == null) {
            resp.sendRedirect(
                    req.getContextPath() + "/restaurantesFiltrados.jsp?error=Restaurante+no+especificado");
            return;
        }
        Long idRestaurante = Long.parseLong(idRestauranteStr);
        Restaurante restaurante = (Restaurante) usuarioDAO.findById(idRestaurante);
        if (restaurante == null) {
            System.out.println("restaurante no encontrado");
        }
        usuarioDAO.save(comensal);

        req.getSession().setAttribute("usuario", comensal);
    }

    private void mostrarPanelRestaurante(HttpServletRequest req, HttpServletResponse resp,
            Restaurante restauranteUsuario) throws ServletException, IOException {
        try {
            // Manejar mensajes de éxito/error
            String success = req.getParameter("success");
            String error = req.getParameter("error");

            if (success != null) {
                req.setAttribute("successMessage", success);
            }
            if (error != null) {
                req.setAttribute("errorMessage", error);
            }

            // Obtener el restaurante actualizado
            Restaurante restaurante = (Restaurante) usuarioDAO.findById(restauranteUsuario.getId());
            req.setAttribute("restaurante", restaurante);

            req.getRequestDispatcher("crearRestaurante.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Error al cargar datos del restaurante: " + e.getMessage());
            req.getRequestDispatcher("crearRestaurante.jsp").forward(req, resp);
        }
    }

    private void procesarGuardarRestaurante(HttpServletRequest req, HttpServletResponse resp,
            Restaurante restauranteUsuario) throws IOException {
        try {
            restauranteUsuario.setNombre(req.getParameter("nombre"));
            restauranteUsuario.setDescripcion(req.getParameter("descripcion"));
            restauranteUsuario.setTipoComida(req.getParameter("tipoComida"));
            restauranteUsuario.setHoraApertura(LocalTime.parse(req.getParameter("horaApertura")));
            restauranteUsuario.setHoraCierre(LocalTime.parse(req.getParameter("horaCierre")));
            restauranteUsuario.setDistanciaUniversidad(
                    Double.parseDouble(req.getParameter("distanciaUniversidad")));
            restauranteUsuario.setPrecio(Integer.parseInt(req.getParameter("precio")));

            // Procesar nuevos campos
            try {
                restauranteUsuario.setTiempoEspera(Integer.parseInt(req.getParameter("tiempoEspera")));
                restauranteUsuario.setCalidad(Integer.parseInt(req.getParameter("calidad")));
                restauranteUsuario.setPrecio(Integer.parseInt(req.getParameter("precio")));
                restauranteUsuario
                        .setDistanciaUniversidad(Double.parseDouble(req.getParameter("distanciaUniversidad")));
            } catch (NumberFormatException e) {
                // Manejar errores de conversión
                resp.sendRedirect(req.getContextPath() + "/restaurante?error=Formato+inválido+en+campos+numéricos");
                return;
            }

            usuarioDAO.save(restauranteUsuario);

            HttpSession session = req.getSession();
            session.setAttribute("usuario", restauranteUsuario);

            resp.sendRedirect(req.getContextPath() + "/restaurante?success=Restaurante+actualizado+exitosamente");
        } catch (Exception e) {
            resp.sendRedirect(
                    req.getContextPath() + "/restaurante?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

    private void procesarAgregarMenu(HttpServletRequest req, HttpServletResponse resp,
            Restaurante restauranteUsuario) throws IOException, ServletException {
        try {
            String menu = req.getParameter("historia");

            if (menu == null || menu.trim().isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/restaurante?error=El+menú+no+puede+estar+vacío");
                return;
            }

            // Obtener el restaurante actualizado
            Restaurante restaurante = (Restaurante) usuarioDAO.findById(restauranteUsuario.getId());
            restaurante.agregarHistoria(menu);

            // Guardar y actualizar la sesión
            usuarioDAO.save(restaurante);
            req.getSession().setAttribute("usuario", restaurante);

            // Redirigir a la misma página con éxito
            resp.sendRedirect(req.getContextPath() + "/restaurante?success=Menú+agregado");
        } catch (Exception e) {
            resp.sendRedirect(
                    req.getContextPath() + "/restaurante?error=" + URLEncoder.encode(e.getMessage(), "UTF-8"));
        }
    }

    private void procesarActualizarRestaurante(HttpServletRequest req, HttpServletResponse resp,
            Restaurante restauranteUsuario) throws IOException {
        try {
            // Actualizar los datos del restaurante con los parámetros recibidos
            String nombre = req.getParameter("nombre");
            String descripcion = req.getParameter("descripcion");
            String tipoComida = req.getParameter("tipoComida");
            String horaApertura = req.getParameter("horaApertura");
            String horaCierre = req.getParameter("horaCierre");
            Double distanciaUniversidad = Double.parseDouble(req.getParameter("distanciaUniversidad"));
            int precio = Integer.parseInt(req.getParameter("precio"));

            if (nombre != null)
                restauranteUsuario.setNombre(nombre);
            if (descripcion != null)
                restauranteUsuario.setDescripcion(descripcion);
            if (tipoComida != null)
                restauranteUsuario.setTipoComida(tipoComida);
            if (horaApertura != null)
                restauranteUsuario.setHoraApertura(LocalTime.parse(horaApertura));
            if (horaCierre != null)
                restauranteUsuario.setHoraCierre(LocalTime.parse(horaCierre));
            if (distanciaUniversidad != null)
                restauranteUsuario.setDistanciaUniversidad(distanciaUniversidad);
            if (precio > 0)
                restauranteUsuario.setPrecio(precio);

            // Procesar nuevos campos
            // if (tiempoEspera != null && !tiempoEspera.isEmpty())
            // restauranteUsuario.setTiempoEspera(Integer.parseInt(tiempoEspera));
            // if (calidad != null && !calidad.isEmpty())
            // restauranteUsuario.setCalidad(Integer.parseInt(calidad));
            // if (precio != null && !precio.isEmpty())
            // restauranteUsuario.setPrecio(Integer.parseInt(precio));
            // if (distanciaUniversidad != null && !distanciaUniversidad.isEmpty())
            // restauranteUsuario.setDistanciaUniversidad(Double.parseDouble(distanciaUniversidad));
            // 1
            // 2
            // 3
            // 4
            // 5
            // 6
            // 7
            // 8

            // 9
            // 10
            // 11
            // 12
            // 13
            // 14
            // Guardar en la base de datos
            usuarioDAO.save(restauranteUsuario);

            // Actualizar el objeto en sesión
            HttpSession session = req.getSession();
            session.setAttribute("usuario", restauranteUsuario);

            // Redirigir a la misma página con parámetro de éxito
            resp.sendRedirect(req.getContextPath() + "/restaurante?success=Restaurante+actualizado+correctamente");
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
