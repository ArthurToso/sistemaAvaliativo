package controller;

import dao.UnidadeCurricularDAO;
import dao.impl.UnidadeCurricularDAOImpl;
import model.UnidadeCurricular;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet que controla as ações do CRUD de UnidadeCurricular.
 * Mapeado para a URL "/unidades"
 */
@WebServlet("/unidades")
public class UnidadeCurricularServlet extends HttpServlet {

    private UnidadeCurricularDAO unidadeCurricularDAO;

    @Override
    public void init() {
        this.unidadeCurricularDAO = new UnidadeCurricularDAOImpl();
    }

    /**
     * Chamado quando a página é carregada (GET).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Busca a lista de UCs
        List<UnidadeCurricular> listaUCs = unidadeCurricularDAO.listarTodos();

        // 2. Coloca a lista no objeto 'request'
        request.setAttribute("listaUCs", listaUCs);

        // 3. Encaminha a requisição para o arquivo JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/unidades.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Chamado quando o formulário é enviado (POST).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Pega o nome da UC enviado pelo formulário
        String nomeUC = request.getParameter("nome");

        // 2. Cria um novo objeto UnidadeCurricular
        if (nomeUC != null && !nomeUC.isEmpty()) {
            UnidadeCurricular novaUC = new UnidadeCurricular(nomeUC);

            // 3. Salva a nova UC no banco
            unidadeCurricularDAO.salvar(novaUC);
        }

        // 4. Redireciona de volta para o próprio servlet (Padrão PRG)
        response.sendRedirect(request.getContextPath() + "/unidades");
    }
}