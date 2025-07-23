package servlets;

import entidades.Usuario;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.MenuDelDiaService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SvMenuDelDia", value = "/SvMenuDelDia")
public class SvMenuDelDia extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuario");
        MenuDelDiaService service = new MenuDelDiaService();
        String idRestaurante = req.getParameter("id");
        
        boolean votoAgregado = service.toggleVoto(Long.valueOf(idRestaurante), usuario);
        
        // Responder con JSON para indicar el estado del voto
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        out.print("{\"votoAgregado\": " + votoAgregado + "}");
        out.flush();
    }
}
