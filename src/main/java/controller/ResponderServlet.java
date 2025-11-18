package controller;

import dao.*;
import dao.impl.*;
import model.*;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@WebServlet("/responder")
public class ResponderServlet extends HttpServlet {

    private FormularioDAO formularioDAO;
    private TurmaDAO turmaDAO;
    private QuestaoDAO questaoDAO;
    private AlternativaDAO alternativaDAO;
    private AvaliacaoRespondidaDAO avaliacaoRespondidaDAO;

    @Override
    public void init() {
        this.formularioDAO = new FormularioDAOImpl();
        this.turmaDAO = new TurmaDAOImpl();
        this.questaoDAO = new QuestaoDAOImpl();
        this.alternativaDAO = new AlternativaDAOImpl();
        this.avaliacaoRespondidaDAO = new AvaliacaoRespondidaDAOImpl();
    }

    /**
     * GET: Exibe o formulário para o aluno responder.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long formularioId = Long.parseLong(request.getParameter("formularioId"));
        Long turmaId = Long.parseLong(request.getParameter("turmaId"));

        Formulario formulario = formularioDAO.buscarPorIdComQuestoesEAlternativas(formularioId);

        request.setAttribute("formulario", formulario);
        request.setAttribute("turmaId", turmaId); // Envia o ID da turma

        RequestDispatcher dispatcher = request.getRequestDispatcher("/responder.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * POST: Salva as respostas do aluno no banco de dados.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario alunoLogado = (Usuario) session.getAttribute("usuarioLogado");

        Long formularioId = Long.parseLong(request.getParameter("formularioId"));
        Long turmaId = Long.parseLong(request.getParameter("turmaId"));

        Formulario formulario = formularioDAO.buscarPorIdComQuestoesEAlternativas(formularioId);
        Turma turma = turmaDAO.buscarPorId(turmaId);

        AvaliacaoRespondida avaliacao = new AvaliacaoRespondida();
        avaliacao.setAluno(alunoLogado);
        avaliacao.setFormulario(formulario);
        avaliacao.setTurma(turma);
        avaliacao.setDataResposta(LocalDateTime.now());

        Set<Resposta> respostas = new HashSet<>();

        // Itera sobre todas as questões do formulário
        for (Questao questao : formulario.getQuestoes()) {

            // O nome do campo no JSP será "q_{id_da_questao}"
            String nomeParametro = "q_" + questao.getId();

            // Pega os valores (pode ser um array para checkboxes)
            String[] valoresResposta = request.getParameterValues(nomeParametro);

            if (valoresResposta == null || valoresResposta.length == 0 || valoresResposta[0].isEmpty()) {
                // Pula esta questão se for opcional e não foi respondida
                // (Adicionar lógica de obrigatoriedade aqui depois)
                continue;
            }

            // --- LÓGICA DE SALVAMENTO CORRIGIDA ---

            if (questao.getTipo() == TipoQuestao.TEXTO) {
                // Se for aberta, salva o texto (só vem um valor)
                Resposta r = new Resposta();
                r.setQuestao(questao);
                r.setAvaliacaoRespondida(avaliacao);
                r.setTextoResposta(valoresResposta[0]);
                respostas.add(r);

            } else if (questao.getTipo() == TipoQuestao.UNICA_ESCOLHA) {
                // Se for radio, salva a alternativa (só vem um valor)
                Resposta r = new Resposta();
                r.setQuestao(questao);
                r.setAvaliacaoRespondida(avaliacao);
                Alternativa alt = alternativaDAO.buscarPorId(Long.parseLong(valoresResposta[0]));
                r.setAlternativaSelecionada(alt);
                respostas.add(r);

            } else if (questao.getTipo() == TipoQuestao.MULTIPLA_ESCOLHA) {
                // Se for checkbox, itera sobre TODOS os valores e cria uma Resposta para CADA
                for (String valorId : valoresResposta) {
                    Resposta r = new Resposta();
                    r.setQuestao(questao);
                    r.setAvaliacaoRespondida(avaliacao);
                    Alternativa alt = alternativaDAO.buscarPorId(Long.parseLong(valorId));
                    r.setAlternativaSelecionada(alt);
                    respostas.add(r);
                }
            }
        }

        avaliacao.setRespostas(respostas);

        try {
            avaliacaoRespondidaDAO.salvar(avaliacao);
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (Exception e) {
            e.printStackTrace();
            // Lógica de erro (aluno já respondeu)
            request.setAttribute("erro", "Erro ao salvar: Você já respondeu este formulário.");

            // Recarrega os dados necessários para a página de "responder"
            Formulario formularioParaRecarregar = formularioDAO.buscarPorIdComQuestoesEAlternativas(formularioId);
            request.setAttribute("formulario", formularioParaRecarregar);
            request.setAttribute("turmaId", turmaId);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/responder.jsp");
            dispatcher.forward(request, response);
        }
    }
}