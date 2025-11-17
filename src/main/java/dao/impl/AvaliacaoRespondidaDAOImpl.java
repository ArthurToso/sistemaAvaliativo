package dao.impl;

import dao.AvaliacaoRespondidaDAO;
import model.AvaliacaoRespondida;
import util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
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
            String jpql = "SELECT ar FROM AvaliacaoRespondida ar " +
                    "WHERE ar.aluno.id = :alunoId " +
                    "AND ar.formulario.id = :formularioId " +
                    "AND ar.turma.id = :turmaId";

            TypedQuery<AvaliacaoRespondida> query = em.createQuery(jpql, AvaliacaoRespondida.class);
            query.setParameter("alunoId", alunoId);
            query.setParameter("formularioId", formularioId);
            query.setParameter("turmaId", turmaId);

            // Usamos Optional para evitar erros de NoResultException
            return Optional.of(query.getSingleResult());

        } catch (NoResultException e) {
            return Optional.empty(); // Nenhum resultado encontrado, o que é normal
        } finally {
            em.close();
        }
    }
}