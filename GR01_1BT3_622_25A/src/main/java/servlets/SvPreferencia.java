package servlets;

import DAO.UsuarioDAOImpl;
import entidades.Restaurante;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import servicios.PreferenciaService;

import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "SvPreferencia", value = "/SvPreferencia")
public class SvPreferencia extends HttpServlet {
    private EntityManagerFactory emf;
    private PreferenciaService preferenciaService;

    @Override
    public void init() {
        emf = Persistence.createEntityManagerFactory("UFood_PU");
        UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl(emf);
        preferenciaService = new PreferenciaService(usuarioDAO);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Map<String, Object> parametrosPreferencia = extraerParametrosPreferencia(req);
        List<Restaurante> restaurantesFiltrados = preferenciaService.aplicarPreferencia(parametrosPreferencia);
        req.setAttribute("restaurantesFiltrados", restaurantesFiltrados);
        req.getRequestDispatcher("restaurantesFiltrados.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Map<String, Object> parametrosPreferencia = extraerParametrosPreferencia(req);
        Long idComensal = (Long) req.getSession().getAttribute("idComensal"); // Obtener ID de la sesión
        String tipoComida = (String) parametrosPreferencia.get("tipoComida");
        LocalTime horaApertura = (LocalTime) parametrosPreferencia.get("horaApertura");
        LocalTime horaCierre = (LocalTime) parametrosPreferencia.get("horaCierre");
        Double distancia = (Double) parametrosPreferencia.get("distancia");

        preferenciaService.crearPreferencia(tipoComida, horaApertura, horaCierre, distancia, idComensal);
        resp.sendRedirect("confirmacionPreferencia.jsp");
    }

    private Map<String, Object> extraerParametrosPreferencia(HttpServletRequest req) {
        Map<String, Object> parametrosPreferencia = new HashMap<>();

        String horaAperturaStr = req.getParameter("horaApertura");
        String horaCierreStr = req.getParameter("horaCierre");
        String distanciaStr = req.getParameter("distancia");
        LocalTime horaApertura = horaAperturaStr != null && !horaAperturaStr.isEmpty()
                ? LocalTime.parse(horaAperturaStr)
                : null;
        LocalTime horaCierre = horaCierreStr != null && !horaCierreStr.isEmpty() ? LocalTime.parse(horaCierreStr)
                : null;
        Double distanciaMax = distanciaStr != null && !distanciaStr.isEmpty() ? Double.parseDouble(distanciaStr) : null;

        parametrosPreferencia.put("tipoComida", req.getParameter("tipoComida"));
        parametrosPreferencia.put("horaApertura", horaApertura);
        parametrosPreferencia.put("horaCierre", horaCierre);
        parametrosPreferencia.put("distancia", distanciaMax);

        return parametrosPreferencia;
    }

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
