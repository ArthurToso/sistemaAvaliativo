package dao.impl;

import dao.UnidadeCurricularDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.UnidadeCurricular;
import util.JPAUtil;

import java.util.List;

public class UnidadeCurricularDAOImpl implements UnidadeCurricularDAO {
    @Override
    public void salvar(UnidadeCurricular unidadeCurricular) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(unidadeCurricular);
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
    public void atualizar(UnidadeCurricular unidadeCurricular) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(unidadeCurricular);
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
    public void deletar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            UnidadeCurricular uc = em.find(UnidadeCurricular.class, id);
            if (uc != null) {
                em.remove(uc);
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
    public UnidadeCurricular buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(UnidadeCurricular.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<UnidadeCurricular> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Note a mudança no JPQL para "UnidadeCurricular"
            String jpql = "SELECT u FROM UnidadeCurricular u";
            TypedQuery<UnidadeCurricular> query = em.createQuery(jpql, UnidadeCurricular.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
