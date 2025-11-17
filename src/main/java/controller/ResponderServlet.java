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

        // Busca o formulário completo, com suas questões e alternativas
        Formulario formulario = formularioDAO.buscarPorId(formularioId);

        // Precisamos forçar o carregamento das questões e alternativas
        // ANTES de enviar para o JSP, para evitar LazyInitializationException.
        // (O DAO de Questao/Alternativa já poderia fazer isso, mas faremos aqui)
        for (Questao q : formulario.getQuestoes()) {
            q.getAlternativas().size(); // Força o carregamento
        }

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

        // 1. Pegar os IDs de contexto do formulário
        Long formularioId = Long.parseLong(request.getParameter("formularioId"));
        Long turmaId = Long.parseLong(request.getParameter("turmaId"));

        // 2. Buscar os objetos "pai" do banco
        Formulario formulario = formularioDAO.buscarPorId(formularioId);
        Turma turma = turmaDAO.buscarPorId(turmaId);

        // 3. Criar a "Capa" da avaliação (RF13)
        AvaliacaoRespondida avaliacao = new AvaliacaoRespondida();
        avaliacao.setAluno(alunoLogado);
        avaliacao.setFormulario(formulario);
        avaliacao.setTurma(turma);
        avaliacao.setDataResposta(LocalDateTime.now());

        // 4. Criar o conjunto de Respostas
        Set<Resposta> respostas = new HashSet<>();

        // 5. Iterar sobre todas as questões do formulário
        for (Questao questao : formulario.getQuestoes()) {
            // O nome do campo no JSP será "q_{id_da_questao}"
            String nomeParametro = "q_" + questao.getId();

            // Pega o valor enviado
            String valorResposta = request.getParameter(nomeParametro);

            if (valorResposta != null && !valorResposta.isEmpty()) {
                Resposta r = new Resposta();
                r.setQuestao(questao);
                r.setAvaliacaoRespondida(avaliacao); // Linka à "capa"

                if (questao.getTipo() == TipoQuestao.TEXTO) {
                    // Se for aberta, salva o texto
                    r.setTextoResposta(valorResposta);
                } else {
                    // Se for de múltipla escolha, busca a alternativa e a salva
                    Alternativa altSelecionada = alternativaDAO.buscarPorId(Long.parseLong(valorResposta));
                    r.setAlternativaSelecionada(altSelecionada);
                }
                respostas.add(r);
            }
            // (Lógica de RF10 - obrigatoriedade - pode ser adicionada aqui)
        }

        // Adiciona o conjunto de respostas preenchido à "capa"
        avaliacao.setRespostas(respostas);

        // 6. Salvar tudo no banco de uma vez
        try {
            avaliacaoRespondidaDAO.salvar(avaliacao);

            // 7. Redirecionar para o home com mensagem de sucesso
            // (Vamos implementar a mensagem de sucesso depois)
            response.sendRedirect(request.getContextPath() + "/home");

        } catch (Exception e) {
            // Isso vai falhar se a UNIQUE KEY (RF13) for violada
            // (Aluno tentando responder de novo)
            e.printStackTrace();
            request.setAttribute("erro", "Você já respondeu este formulário.");
            // Recarrega a página de formulários do aluno
            doGet(request, response);
        }
    }
}