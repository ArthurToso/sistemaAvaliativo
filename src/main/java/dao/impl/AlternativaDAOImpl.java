package dao.impl;

import dao.AlternativaDAO;
import model.Alternativa;
import util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class AlternativaDAOImpl implements AlternativaDAO {

    @Override
    public void salvar(Alternativa alternativa) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Merge é mais seguro para garantir a associação com a Questão
            em.merge(alternativa);
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
    public void atualizar(Alternativa alternativa) {
        salvar(alternativa); // Merge já lida com atualizações
    }

    @Override
    public void deletar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Alternativa alternativa = em.find(Alternativa.class, id);
            if (alternativa != null) {
                em.remove(alternativa);
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
    public Alternativa buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Alternativa.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Alternativa> listarPorQuestao(Long questaoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT a FROM Alternativa a WHERE a.questao.id = :questaoId";
            TypedQuery<Alternativa> query = em.createQuery(jpql, Alternativa.class);
            query.setParameter("questaoId", questaoId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}