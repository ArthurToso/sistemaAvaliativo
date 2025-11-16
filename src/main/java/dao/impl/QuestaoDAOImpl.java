package dao.impl;

import dao.QuestaoDAO;
import model.Questao;
import util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class QuestaoDAOImpl implements QuestaoDAO {

    @Override
    public void salvar(Questao questao) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // O 'merge' é mais seguro aqui para garantir que a entidade
            // 'formulario' que está dentro da 'questao' seja gerenciada.
            em.merge(questao);
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
    public void atualizar(Questao questao) {
        salvar(questao); // Merge já lida com atualizações
    }

    @Override
    public void deletar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Questao questao = em.find(Questao.class, id);
            if (questao != null) {
                em.remove(questao);
            }
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
    public Questao buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Questao.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Questao> listarPorFormulario(Long formularioId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT q FROM Questao q WHERE q.formulario.id = :formularioId";
            TypedQuery<Questao> query = em.createQuery(jpql, Questao.class);
            query.setParameter("formularioId", formularioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}