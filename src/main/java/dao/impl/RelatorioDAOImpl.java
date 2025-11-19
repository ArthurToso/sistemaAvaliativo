package dao.impl;

import dao.RelatorioDAO;
import model.dto.EstatisticaQuestao;
import model.Formulario;
import model.Questao;
import model.Resposta;
import model.TipoQuestao;
import org.hibernate.Hibernate;
import util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelatorioDAOImpl implements RelatorioDAO {

    @Override
    public List<EstatisticaQuestao> gerarRelatorioTurma(Long formularioId, Long turmaId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // 1. Busca o formulário e suas questões para ter a estrutura base
            Formulario formulario = em.find(Formulario.class, formularioId);

            // Mapa auxiliar para organizar as estatísticas por ID da questão
            Map<Long, EstatisticaQuestao> mapaEstatisticas = new HashMap<>();

            // Inicializa o mapa E carrega as alternativas
            for (Questao q : formulario.getQuestoes()) {
                // Esta linha força o Hibernate a ir ao banco buscar as alternativas AGORA
                Hibernate.initialize(q.getAlternativas());

                mapaEstatisticas.put(q.getId(), new EstatisticaQuestao(q));
            }

            // 2. Busca TODAS as respostas dadas para esse formulário nessa turma
            String jpql = "SELECT r FROM Resposta r " +
                    "JOIN r.avaliacaoRespondida ar " +
                    "WHERE ar.formulario.id = :formularioId " +
                    "AND ar.turma.id = :turmaId";

            TypedQuery<Resposta> query = em.createQuery(jpql, Resposta.class);
            query.setParameter("formularioId", formularioId);
            query.setParameter("turmaId", turmaId);

            List<Resposta> todasRespostas = query.getResultList();

            // 3. Processa as respostas e preenche os DTOs
            for (Resposta r : todasRespostas) {
                EstatisticaQuestao dto = mapaEstatisticas.get(r.getQuestao().getId());

                if (dto != null) {
                    if (r.getQuestao().getTipo() == TipoQuestao.TEXTO) {
                        dto.adicionarRespostaTexto(r.getId(), r.getTextoResposta());
                    } else {
                        // Múltipla escolha ou Única
                        if (r.getAlternativaSelecionada() != null) {
                            dto.adicionarVotoAlternativa(r.getAlternativaSelecionada().getId());
                        }
                    }
                }
            }

            // Retorna a lista de estatísticas
            return new ArrayList<>(mapaEstatisticas.values());

        } finally {
            em.close();
        }
    }
}