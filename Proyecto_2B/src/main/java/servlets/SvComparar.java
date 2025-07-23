package servlets;

import DAO.CalificacionDAO;
import DAO.RestauranteDAO;
import entidades.Restaurante;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servicios.ComparacionService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "comparar", urlPatterns = {"/comparar"})
public class SvComparar extends HttpServlet {
    private RestauranteDAO restauranteDAO;
    private EntityManagerFactory emf;

    @Override
    public void init() {
        emf = Persistence.createEntityManagerFactory("UFood_PU");
        restauranteDAO = new RestauranteDAO(emf);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");

        try {
            if (accion == null || accion.equals("listar")) {
                List<Restaurante> restaurantes = restauranteDAO.obtenerTodosRestaurantes();
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

                // Obtener restaurantes usando el RestauranteDAO
                Restaurante rest1 = restauranteDAO.obtenerRestaurantePorId(id1);
                Restaurante rest2 = restauranteDAO.obtenerRestaurantePorId(id2);

                if (rest1 == null || rest2 == null) {
                    request.setAttribute("error", "Uno o ambos restaurantes no fueron encontrados");
                    doGet(request, response);
                    return;
                }

                // Calcular promedios de atributos
                CalificacionDAO calificacionDAO = new CalificacionDAO(emf);
                String[] atributos = {
                    "calidadComida", "calidadServicio", "limpieza", "ambiente",
                    "tiempoEspera", "relacionPrecioCalidad", "variedadMenu", "accesibilidad", "volveria"
                };

                double[] promediosRest1 = new double[atributos.length];
                double[] promediosRest2 = new double[atributos.length];

                for (int i = 0; i < atributos.length; i++) {
                    promediosRest1[i] = calificacionDAO.calcularPromedioAtributo(id1, atributos[i]);
                    promediosRest2[i] = calificacionDAO.calcularPromedioAtributo(id2, atributos[i]);
                }

                request.getSession().setAttribute("promediosRest1", promediosRest1);
                request.getSession().setAttribute("promediosRest2", promediosRest2);

                request.getSession().setAttribute("restaurante1", rest1);
                request.getSession().setAttribute("restaurante2", rest2);

                response.sendRedirect(request.getContextPath() + "/resultadoComparacion.jsp");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Ha ocurrido un error en el proceso");
            List<Restaurante> restaurantes = restauranteDAO.obtenerTodosRestaurantes();
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