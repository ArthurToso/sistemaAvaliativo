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
import java.util.HashMap; // Importar
import java.util.HashSet;
import java.util.Map; // Importar
import java.util.Optional; // Importar
import java.util.Set; // Importar

@WebServlet("/responder")
public class ResponderServlet extends HttpServlet {

    private FormularioDAO formularioDAO;
    private TurmaDAO turmaDAO;
    private AlternativaDAO alternativaDAO;
    private AvaliacaoRespondidaDAO avaliacaoRespondidaDAO;

    @Override
    public void init() {
        this.formularioDAO = new FormularioDAOImpl();
        this.turmaDAO = new TurmaDAOImpl();
        this.alternativaDAO = new AlternativaDAOImpl();
        this.avaliacaoRespondidaDAO = new AvaliacaoRespondidaDAOImpl();
        // Não precisamos do QuestaoDAO aqui, pois as questões vêm com o formulário
    }

    /**
     * GET: Exibe o formulário, pré-preenchendo se for edição.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long formularioId = Long.parseLong(request.getParameter("formularioId"));
        Long turmaId = Long.parseLong(request.getParameter("turmaId"));
        Usuario alunoLogado = (Usuario) request.getSession().getAttribute("usuarioLogado");

        // 1. Busca o formulário completo (com questões e alternativas)
        Formulario formulario = formularioDAO.buscarPorIdComQuestoesEAlternativas(formularioId);

        // 2. Verifica se o aluno já respondeu (RF13 - Lógica de Edição)
        Optional<AvaliacaoRespondida> opAvaliacao = avaliacaoRespondidaDAO
                .buscarPorAlunoEFormulario(alunoLogado.getId(), formularioId, turmaId);

        if (opAvaliacao.isPresent()) {
            // --- MODO DE EDIÇÃO ---
            AvaliacaoRespondida avaliacao = opAvaliacao.get();

            // Se for anônimo, não pode editar (RF13)
            if (avaliacao.getFormulario().getTipo() == TipoFormulario.ANONIMO) {
                // Redireciona de volta com erro
                response.sendRedirect(request.getContextPath() + "/home?erro=anonimo");
                return;
            }

            // Cria mapas para o JSP pré-preencher
            Map<Long, String> respostasTexto = new HashMap<>();
            Map<Long, Long> respostasUnica = new HashMap<>();
            Map<Long, Set<Long>> respostasMulti = new HashMap<>();

            for (Resposta r : avaliacao.getRespostas()) {
                Long questaoId = r.getQuestao().getId();
                if (r.getQuestao().getTipo() == TipoQuestao.TEXTO) {
                    respostasTexto.put(questaoId, r.getTextoResposta());
                } else if (r.getQuestao().getTipo() == TipoQuestao.UNICA_ESCOLHA) {
                    respostasUnica.put(questaoId, r.getAlternativaSelecionada().getId());
                } else if (r.getQuestao().getTipo() == TipoQuestao.MULTIPLA_ESCOLHA) {
                    respostasMulti.computeIfAbsent(questaoId, k -> new HashSet<>())
                            .add(r.getAlternativaSelecionada().getId());
                }
            }

            request.setAttribute("respostasTexto", respostasTexto);
            request.setAttribute("respostasUnica", respostasUnica);
            request.setAttribute("respostasMulti", respostasMulti);
            // Envia o ID da avaliação existente para o POST saber que é um UPDATE
            request.setAttribute("avaliacaoId", avaliacao.getId());
        }
        // else: Modo de Criação (os mapas estarão vazios, o JSP vai lidar com isso)

        request.setAttribute("formulario", formulario);
        request.setAttribute("turmaId", turmaId);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/responder.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * POST: Salva (Cria ou Atualiza) as respostas.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        Usuario alunoLogado = (Usuario) session.getAttribute("usuarioLogado");

        // Pegar dados do formulário
        Long formularioId = Long.parseLong(request.getParameter("formularioId"));
        Long turmaId = Long.parseLong(request.getParameter("turmaId"));
        String avaliacaoIdStr = request.getParameter("avaliacaoId"); // Vem se for edição

        Formulario formulario = formularioDAO.buscarPorIdComQuestoesEAlternativas(formularioId);

        AvaliacaoRespondida avaliacao;

        // --- LÓGICA DE UPDATE (RF13) ---
        if (avaliacaoIdStr != null && !avaliacaoIdStr.isEmpty()) {
            // MODO DE ATUALIZAÇÃO
            // Busca a avaliação existente (com suas respostas)
            Long avaliacaoId = Long.parseLong(avaliacaoIdStr);
            avaliacao = avaliacaoRespondidaDAO.buscarPorIdComRespostas(avaliacaoId).get();

            // Limpa as respostas antigas. O Cascade.ALL vai deletá-las
            avaliacao.getRespostas().clear();
        } else {
            // MODO DE CRIAÇÃO
            avaliacao = new AvaliacaoRespondida();
            avaliacao.setAluno(alunoLogado);
            avaliacao.setFormulario(formulario);
            Turma turma = turmaDAO.buscarPorId(turmaId);
            avaliacao.setTurma(turma);
        }

        // Atualiza a data da resposta
        avaliacao.setDataResposta(LocalDateTime.now());

        Set<Resposta> novasRespostas = new HashSet<>();

        // Loop de salvamento (igual ao que fizemos antes)
        for (Questao questao : formulario.getQuestoes()) {
            String nomeParametro = "q_" + questao.getId();
            String[] valoresResposta = request.getParameterValues(nomeParametro);

            if (valoresResposta == null || valoresResposta.length == 0 || valoresResposta[0].isEmpty()) {
                continue; // Pula não respondidas
            }

            if (questao.getTipo() == TipoQuestao.TEXTO) {
                Resposta r = new Resposta();
                r.setQuestao(questao);
                r.setAvaliacaoRespondida(avaliacao);
                r.setTextoResposta(valoresResposta[0]);
                novasRespostas.add(r);
            } else { // UNICA_ESCOLHA ou MULTIPLA_ESCOLHA
                for (String valorId : valoresResposta) {
                    Resposta r = new Resposta();
                    r.setQuestao(questao);
                    r.setAvaliacaoRespondida(avaliacao);
                    Alternativa alt = alternativaDAO.buscarPorId(Long.parseLong(valorId));
                    r.setAlternativaSelecionada(alt);
                    novasRespostas.add(r);
                }
            }
        }

        // Seta o novo conjunto de respostas na "capa"
        avaliacao.setRespostas(novasRespostas);

        // Salva (o 'merge' do DAO cuida de criar ou atualizar)
        try {
            avaliacaoRespondidaDAO.salvar(avaliacao);
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (Exception e) {
            // Erro (ex: violação de constraint, etc.)
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/home?erro=salvar");
        }
    }
}