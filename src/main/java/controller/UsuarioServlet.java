package controller;

import dao.PerfilDAO;
import dao.UsuarioDAO;
import dao.impl.PerfilDAOImpl;
import dao.impl.UsuarioDAOImpl;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Perfil;
import model.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet("/usuarios")
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private PerfilDAO perfilDAO;

    @Override
    public void init() {
        // Instancia os DAOs
        this.usuarioDAO = new UsuarioDAOImpl();
        this.perfilDAO = new PerfilDAOImpl();
    }

    /**
     * Chamado quando a página é carregada (GET).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Busca a lista de USUÁRIOS
        List<Usuario> listaUsuarios = usuarioDAO.listarTodos();



        // 2. Busca a lista de PERFIS (para o dropdown)
        List<Perfil> listaPerfis = perfilDAO.listarTodos();

        // 3. Coloca AMBAS as listas no objeto 'request'
        request.setAttribute("listaUsuarios", listaUsuarios);
        request.setAttribute("listaPerfis", listaPerfis);

        // 4. Encaminha a requisição para o arquivo JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/usuarios.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Chamado quando o formulário é enviado (POST).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Pega os dados do formulário
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String login = request.getParameter("login");
        String senha = request.getParameter("senha"); // Em um projeto real, faríamos hash da senha
        Long perfilId = Long.parseLong(request.getParameter("perfilId"));

        // 2. Busca o objeto Perfil completo usando o ID
        Perfil perfil = perfilDAO.buscarPorId(perfilId);

        // 3. Cria o novo objeto Usuario com o Perfil
        if (perfil != null) {
            Usuario novoUsuario = new Usuario();
            novoUsuario.setNome(nome);
            novoUsuario.setEmail(email);
            novoUsuario.setLogin(login);
            novoUsuario.setSenha(senha);
            novoUsuario.setPerfil(perfil); // Associa o objeto Perfil

            // 4. Salva o novo usuário no banco
            usuarioDAO.salvar(novoUsuario);
        }

        // 5. Redireciona de volta para o próprio servlet (padrão PRG)
        response.sendRedirect(request.getContextPath() + "/usuarios");
    }
}