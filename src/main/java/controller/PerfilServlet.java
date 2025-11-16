package controller;

import dao.PerfilDAO;
import dao.impl.PerfilDAOImpl;
import model.Perfil;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/perfis")
public class PerfilServlet extends HttpServlet {

    private PerfilDAO perfilDAO;

    @Override
    public void init() {
        this.perfilDAO = new PerfilDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Perfil> listaPerfis = perfilDAO.listarTodos();
        request.setAttribute("listaPerfis", listaPerfis);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/perfis.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nomePerfil = request.getParameter("nome");

        if (nomePerfil != null && !nomePerfil.isEmpty()) {
            Perfil novoPerfil = new Perfil(nomePerfil);
            perfilDAO.salvar(novoPerfil);
        }

        response.sendRedirect(request.getContextPath() + "/perfis");
    }
}