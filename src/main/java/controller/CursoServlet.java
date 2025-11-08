package controller;

import dao.CursoDAO;
import dao.impl.CursoDAOImpl;
import model.Curso;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet que controla as ações do CRUD de Curso.
 * Mapeado para a URL "/cursos"
 */
@WebServlet("/cursos")
public class CursoServlet extends HttpServlet {

    private CursoDAO cursoDAO;

    // Inicializa o DAO quando o servlet é iniciado
    @Override
    public void init() {
        // Instancia a implementação do DAO
        this.cursoDAO = new CursoDAOImpl();
    }

    /**
     * Chamado quando a página é carregada (GET).
     * Responsável por listar os cursos.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Busca a lista de cursos no banco de dados
        List<Curso> listaCursos = cursoDAO.listarTodos();

        // 2. Coloca a lista no objeto 'request' para ser acessível pelo JSP
        request.setAttribute("listaCursos", listaCursos);

        // 3. Encaminha a requisição para o arquivo JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/cursos.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Chamado quando o formulário é enviado (POST).
     * Responsável por salvar um novo curso.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Pega o nome do curso enviado pelo formulário
        String nomeCurso = request.getParameter("nome");

        // 2. Cria um novo objeto Curso (sem ID, pois é novo)
        if (nomeCurso != null && !nomeCurso.isEmpty()) {
            Curso novoCurso = new Curso(nomeCurso);

            // 3. Salva o novo curso no banco de dados
            cursoDAO.salvar(novoCurso);
        }

        // 4. Redireciona de volta para o próprio servlet (URL /cursos)
        // Isso executa o doGet novamente, que vai buscar a lista atualizada
        // (Este é o padrão Post-Redirect-Get - PRG)
        response.sendRedirect(request.getContextPath() + "/cursos");
    }
}