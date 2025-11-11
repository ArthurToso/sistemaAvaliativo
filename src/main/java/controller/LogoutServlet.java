package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // Obtém a sessão atual, se existir
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate(); // Destrói a sessão
        }

        // Redireciona para a tela de login
        response.sendRedirect(request.getContextPath() + "/login");
    }
}