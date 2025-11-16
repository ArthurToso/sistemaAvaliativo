package controller;

import dao.ProcessoAvaliativoDAO;
import dao.TurmaDAO;
import dao.impl.ProcessoAvaliativoDAOImpl;
import dao.impl.TurmaDAOImpl;
import model.ProcessoAvaliativo;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Turma;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/processos")
public class ProcessoAvaliativoServlet extends HttpServlet {

    private ProcessoAvaliativoDAO processoAvaliativoDAO;
    private TurmaDAO turmaDAO;

    @Override
    public void init() {
        this.processoAvaliativoDAO = new ProcessoAvaliativoDAOImpl();
        this.turmaDAO = new TurmaDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<ProcessoAvaliativo> listaProcessos = processoAvaliativoDAO.listarTodos();
        request.setAttribute("listaProcessos", listaProcessos);

        List<Turma> listaTurmas = turmaDAO.listarTodos();
        request.setAttribute("listaTurmas", listaTurmas);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/processos.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Pega os dados do formulário
        String titulo = request.getParameter("titulo");
        String dataInicioStr = request.getParameter("dataInicio");
        String dataFimStr = request.getParameter("dataFim");

        String[] turmaIds = request.getParameterValues("turmaIds");

        // 2. Converte as strings de data para LocalDate
        LocalDate dataInicio = LocalDate.parse(dataInicioStr);
        LocalDate dataFim = LocalDate.parse(dataFimStr);

        Set<Turma> turmasSelecionadas = new HashSet<>();
        if (turmaIds != null) {
            for (String id : turmaIds) {
                Turma turma = turmaDAO.buscarPorId(Long.parseLong(id));
                if (turma != null) {
                    turmasSelecionadas.add(turma);
                }
            }
        }

        // 3. Cria o novo objeto
        ProcessoAvaliativo novoProcesso = new ProcessoAvaliativo();
        novoProcesso.setTitulo(titulo);
        novoProcesso.setDataInicio(dataInicio);
        novoProcesso.setDataFim(dataFim);
        novoProcesso.setTurmas(turmasSelecionadas);

        // 4. Salva no banco
        processoAvaliativoDAO.salvar(novoProcesso);

        // 5. Redireciona de volta (Padrão PRG)
        response.sendRedirect(request.getContextPath() + "/processos");
    }
}