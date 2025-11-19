package controller;

import dao.FormularioDAO;
import dao.RelatorioDAO;
import dao.TurmaDAO;
import dao.impl.FormularioDAOImpl;
import dao.impl.RelatorioDAOImpl;
import dao.impl.TurmaDAOImpl;
import model.dto.EstatisticaQuestao;
import model.Formulario;
import model.Turma;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/relatorio/turma")
public class RelatorioServlet extends HttpServlet {

    private RelatorioDAO relatorioDAO;
    private FormularioDAO formularioDAO;
    private TurmaDAO turmaDAO;

    @Override
    public void init() {
        this.relatorioDAO = new RelatorioDAOImpl();
        this.formularioDAO = new FormularioDAOImpl();
        this.turmaDAO = new TurmaDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Pega os parâmetros da URL
        String formIdStr = request.getParameter("formularioId");
        String turmaIdStr = request.getParameter("turmaId");

        if (formIdStr != null && turmaIdStr != null) {
            Long formularioId = Long.parseLong(formIdStr);
            Long turmaId = Long.parseLong(turmaIdStr);

            // 2. Busca os objetos de contexto (para exibir no cabeçalho)
            Formulario formulario = formularioDAO.buscarPorId(formularioId);
            Turma turma = turmaDAO.buscarPorIdComListas(turmaId);

            // 3. GERA O RELATÓRIO (Chama o DAO complexo)
            List<EstatisticaQuestao> estatisticas = relatorioDAO.gerarRelatorioTurma(formularioId, turmaId);

            // 4. Envia tudo para o JSP
            request.setAttribute("formulario", formulario);
            request.setAttribute("turma", turma);
            request.setAttribute("estatisticas", estatisticas);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/relatorio_turma.jsp");
            dispatcher.forward(request, response);
        } else {
            // Se faltar parâmetro, volta para o dashboard
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}