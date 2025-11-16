package controller;

import dao.AlternativaDAO;
import dao.QuestaoDAO;
import dao.impl.AlternativaDAOImpl;
import dao.impl.QuestaoDAOImpl;
import model.Alternativa;
import model.Questao;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/alternativas")
public class AlternativaServlet extends HttpServlet {

    private AlternativaDAO alternativaDAO;
    private QuestaoDAO questaoDAO; // Para buscar a questão "pai"

    @Override
    public void init() {
        this.alternativaDAO = new AlternativaDAOImpl();
        this.questaoDAO = new QuestaoDAOImpl();
    }

    /**
     * Chamado quando a página é carregada (GET).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Pega o ID da questão que veio da URL
        Long questaoId = Long.parseLong(request.getParameter("questaoId"));

        // 2. Busca a questão "pai" no banco (para exibir o enunciado)
        Questao questao = questaoDAO.buscarPorId(questaoId);

        // 3. Busca a lista de alternativas "filhas"
        List<Alternativa> listaAlternativas = alternativaDAO.listarPorQuestao(questaoId);

        // 4. Coloca a questão e a lista no request
        request.setAttribute("questao", questao);
        request.setAttribute("listaAlternativas", listaAlternativas);

        // 5. Encaminha para o JSP de alternativas
        RequestDispatcher dispatcher = request.getRequestDispatcher("/alternativas.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Chamado quando o formulário de NOVA ALTERNATIVA é enviado (POST).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Pega os dados da nova alternativa
        String texto = request.getParameter("texto");
        double peso = Double.parseDouble(request.getParameter("peso")); // RF17

        // 2. Pega o ID da questão pai (campo escondido)
        Long questaoId = Long.parseLong(request.getParameter("questaoId"));

        // 3. Busca o objeto Questao "pai"
        Questao questaoPai = questaoDAO.buscarPorId(questaoId);

        // 4. Cria a nova Alternativa e associa ao pai
        Alternativa novaAlternativa = new Alternativa();
        novaAlternativa.setTexto(texto);
        novaAlternativa.setPeso(peso);
        novaAlternativa.setQuestao(questaoPai); // Linka a alternativa à questão

        // 5. Salva a nova alternativa
        alternativaDAO.salvar(novaAlternativa);

        // 6. Redireciona de volta para a mesma página (padrão PRG)
        response.sendRedirect(request.getContextPath() + "/alternativas?questaoId=" + questaoId);
    }
}