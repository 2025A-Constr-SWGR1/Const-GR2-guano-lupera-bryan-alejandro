package servlets;

import DAO.UsuarioDAO;
import DAO.UsuarioDAOImpl;
import entidades.Restaurante;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "comparar", urlPatterns = {"/comparar"})
public class SvComparar extends HttpServlet {
    private UsuarioDAO usuarioDAO;
    private EntityManagerFactory emf;

    @Override
    public void init() {
        emf = Persistence.createEntityManagerFactory("UFood_PU");
        usuarioDAO = new UsuarioDAOImpl(emf);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        try {
            if (accion == null || accion.equals("listar")) {
                List<Restaurante> restaurantes = usuarioDAO.obtenerTodosRestaurantes();
                request.setAttribute("restaurantes", restaurantes);
                request.getRequestDispatcher("/compararRestaurantes.jsp")
                        .forward(request, response);

            } else if (accion.equals("comparar")) {
                Long id1 = Long.parseLong(request.getParameter("restaurante1"));
                Long id2 = Long.parseLong(request.getParameter("restaurante2"));

                if (id1.equals(id2)) {
                    request.setAttribute("error", "Por favor seleccione dos restaurantes diferentes");
                    doGet(request, response);
                    return;
                }

                // Obtener restaurantes usando el DAO
                Restaurante rest1 = (Restaurante) usuarioDAO.findById(id1);
                Restaurante rest2 = (Restaurante) usuarioDAO.findById(id2);

                if (rest1 == null || rest2 == null) {
                    request.setAttribute("error", "Uno o ambos restaurantes no fueron encontrados");
                    doGet(request, response);
                    return;
                }

                request.getSession().setAttribute("restaurante1", rest1);
                request.getSession().setAttribute("restaurante2", rest2);

                response.sendRedirect(request.getContextPath() + "/resultadoComparacion.jsp");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Ha ocurrido un error en el proceso");
            List<Restaurante> restaurantes = usuarioDAO.obtenerTodosRestaurantes();
            request.setAttribute("restaurantes", restaurantes);
            request.getRequestDispatcher("/compararRestaurantes.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}