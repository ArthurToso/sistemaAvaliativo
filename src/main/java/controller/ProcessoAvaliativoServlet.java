package controller;

import dao.ProcessoAvaliativoDAO;
import dao.impl.ProcessoAvaliativoDAOImpl;
import model.ProcessoAvaliativo;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/processos")
public class ProcessoAvaliativoServlet extends HttpServlet {

    private ProcessoAvaliativoDAO processoAvaliativoDAO;

    @Override
    public void init() {
        this.processoAvaliativoDAO = new ProcessoAvaliativoDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<ProcessoAvaliativo> listaProcessos = processoAvaliativoDAO.listarTodos();
        request.setAttribute("listaProcessos", listaProcessos);

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

        // 2. Converte as strings de data para LocalDate
        LocalDate dataInicio = LocalDate.parse(dataInicioStr);
        LocalDate dataFim = LocalDate.parse(dataFimStr);

        // 3. Cria o novo objeto
        ProcessoAvaliativo novoProcesso = new ProcessoAvaliativo();
        novoProcesso.setTitulo(titulo);
        novoProcesso.setDataInicio(dataInicio);
        novoProcesso.setDataFim(dataFim);

        // 4. Salva no banco
        processoAvaliativoDAO.salvar(novoProcesso);

        // 5. Redireciona de volta (Padrão PRG)
        response.sendRedirect(request.getContextPath() + "/processos");
    }
}