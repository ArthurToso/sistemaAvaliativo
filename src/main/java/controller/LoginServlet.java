package controller;

import dao.UsuarioDAO;
import dao.impl.UsuarioDAOImpl;
import model.Usuario;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    // Exibe a página de login quando acessado via GET
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
        dispatcher.forward(request, response);
    }

    // Processa o formulário de login quando enviado via POST
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String login = request.getParameter("login");
        String senha = request.getParameter("senha");

        // 1. Busca o usuário no banco pelo login
        Usuario usuario = usuarioDAO.buscarPorLogin(login);

        // 2. Verifica se o usuário existe E se a senha bate
        // (Em um sistema real, você usaria BCrypt ou similar para comparar hash de senha)
        if (usuario != null && usuario.getSenha().equals(senha)) {

            // LOGIN SUCESSO!
            // Cria uma sessão HTTP para "lembrar" que este usuário está logado
            HttpSession session = request.getSession();
            session.setAttribute("usuarioLogado", usuario);

            response.sendRedirect(request.getContextPath() + "/home");

        } else {

            // LOGIN FALHOU!
            // Define uma mensagem de erro e volta para a página de login
            request.setAttribute("erroLogin", "Usuário ou senha inválidos!");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
        }
    }
}