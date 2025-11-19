package dao.impl;

import dao.AvaliacaoRespondidaDAO;
import model.AvaliacaoRespondida;
import model.TipoFormulario;
import util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class AvaliacaoRespondidaDAOImpl implements AvaliacaoRespondidaDAO {

    @Override
    public void salvar(AvaliacaoRespondida avaliacao) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Usamos 'merge' para salvar a entidade principal (AvaliacaoRespondida)
            // e suas relações "filhas" (Respostas), graças ao CascadeType.ALL
            em.merge(avaliacao);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<AvaliacaoRespondida> buscarPorAlunoEFormulario(Long alunoId, Long formularioId, Long turmaId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Adicionamos "LEFT JOIN FETCH ar.respostas"
            String jpql = "SELECT ar FROM AvaliacaoRespondida ar " +
                    "LEFT JOIN FETCH ar.respostas " + // <-- ADIÇÃO
                    "WHERE ar.aluno.id = :alunoId " +
                    "AND ar.formulario.id = :formularioId " +
                    "AND ar.turma.id = :turmaId";

            TypedQuery<AvaliacaoRespondida> query = em.createQuery(jpql, AvaliacaoRespondida.class);
            query.setParameter("alunoId", alunoId);
            query.setParameter("formularioId", formularioId);
            query.setParameter("turmaId", turmaId);

            return Optional.of(query.getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    @Override
    public List<AvaliacaoRespondida> listarRespondidasEditaveis(Long alunoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Busca avaliações respondidas (ar) onde o aluno é o logado
            // E o formulário (f) é do tipo IDENTIFICADO (não-anônimo).
            // Já carrega (FETCH) o formulário e a turma para evitar LazyException no JSP.
            String jpql = "SELECT ar FROM AvaliacaoRespondida ar " +
                    "JOIN FETCH ar.formulario f " +
                    "JOIN FETCH ar.turma t " +
                    "WHERE ar.aluno.id = :alunoId " +
                    "AND f.tipo = :tipoIdentificado";

            TypedQuery<AvaliacaoRespondida> query = em.createQuery(jpql, AvaliacaoRespondida.class);
            query.setParameter("alunoId", alunoId);
            query.setParameter("tipoIdentificado", TipoFormulario.IDENTIFICADO); // Passa o Enum
            return query.getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public Optional<AvaliacaoRespondida> buscarPorIdComRespostas(Long avaliacaoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT ar FROM AvaliacaoRespondida ar " +
                    "LEFT JOIN FETCH ar.respostas r " +
                    "LEFT JOIN FETCH r.alternativaSelecionada " + // Também carrega a alternativa
                    "WHERE ar.id = :avaliacaoId";

            TypedQuery<AvaliacaoRespondida> query = em.createQuery(jpql, AvaliacaoRespondida.class);
            query.setParameter("avaliacaoId", avaliacaoId);
            return Optional.of(query.getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty();
        } finally {
            em.close();
        }
    }
}