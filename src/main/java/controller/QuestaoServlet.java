package controller;

import dao.FormularioDAO;
import dao.QuestaoDAO;
import dao.impl.FormularioDAOImpl;
import dao.impl.QuestaoDAOImpl;
import model.Formulario;
import model.Questao;
import model.TipoQuestao; // Importe o Enum

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/questoes")
public class QuestaoServlet extends HttpServlet {

    private QuestaoDAO questaoDAO;
    private FormularioDAO formularioDAO; // Precisamos dele para buscar o formulário "pai"

    @Override
    public void init() {
        this.questaoDAO = new QuestaoDAOImpl();
        this.formularioDAO = new FormularioDAOImpl();
    }

    /**
     * Chamado quando a página é carregada (GET).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Pega o ID do formulário que veio da URL
        Long formularioId = Long.parseLong(request.getParameter("formularioId"));

        // 2. Busca o formulário "pai" no banco
        Formulario formulario = formularioDAO.buscarPorId(formularioId);

        // 3. Busca a lista de questões filhas
        List<Questao> listaQuestoes = questaoDAO.listarPorFormulario(formularioId);

        // 4. Disponibiliza os valores do Enum TipoQuestao para o JSP (RF08, RF09)
        request.setAttribute("tiposQuestao", TipoQuestao.values());

        // 5. Coloca o formulário e a lista no request
        request.setAttribute("formulario", formulario);
        request.setAttribute("listaQuestoes", listaQuestoes);

        // 6. Encaminha para o JSP de questões
        RequestDispatcher dispatcher = request.getRequestDispatcher("/questoes.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Chamado quando o formulário de NOVA QUESTÃO é enviado (POST).
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Pega os dados da nova questão
        String enunciado = request.getParameter("enunciado");
        TipoQuestao tipo = TipoQuestao.valueOf(request.getParameter("tipo"));

        // O 'getParameter' para checkbox retorna "on" ou null.
        boolean obrigatoria = "on".equals(request.getParameter("obrigatoria")); // RF10

        // 2. Pega o ID do formulário pai (que estava num campo escondido)
        Long formularioId = Long.parseLong(request.getParameter("formularioId"));

        // 3. Busca o objeto Formulario "pai"
        Formulario formularioPai = formularioDAO.buscarPorId(formularioId);

        // 4. Cria a nova Questao e associa ao pai
        Questao novaQuestao = new Questao();
        novaQuestao.setEnunciado(enunciado);
        novaQuestao.setTipo(tipo);
        novaQuestao.setObrigatoria(obrigatoria);
        novaQuestao.setFormulario(formularioPai); // Linka a questão ao formulário

        // 5. Salva a nova questão
        questaoDAO.salvar(novaQuestao);

        // 6. Redireciona de volta para a mesma página (padrão PRG)
        //    É crucial passar o formularioId de volta na URL!
        response.sendRedirect(request.getContextPath() + "/questoes?formularioId=" + formularioId);
    }
}