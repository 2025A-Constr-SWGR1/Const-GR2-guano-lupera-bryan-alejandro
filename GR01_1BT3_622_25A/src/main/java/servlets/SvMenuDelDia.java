package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import servicios.MenuDelDiaService;

import java.io.IOException;

@WebServlet(name = "SvMenuDelDia", value = "/SvMenuDelDia")
public class SvMenuDelDia extends HttpServlet {


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("SvMenuDelDia");
        HttpSession session = req.getSession(false);

        // Validar sesión
        if (session == null || session.getAttribute("usuario") == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }



        MenuDelDiaService service = new MenuDelDiaService();
        String idRestauranate = req.getParameter("id");
        service.sumarVoto(Long.valueOf(idRestauranate));

    }

}
