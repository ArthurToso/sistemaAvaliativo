package dao.impl;

import dao.PerfilDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.Perfil;
import util.JPAUtil;

import java.util.List;

public class PerfilDAOImpl implements PerfilDAO {

    @Override
    public void salvar(Perfil perfil) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(perfil);
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
    public void atualizar(Perfil perfil) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(perfil);
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
            Perfil perfil = em.find(Perfil.class, id);
            if (perfil != null) {
                em.remove(perfil);
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
    public Perfil buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Perfil.class, id);
        } finally {
            em.close();
        }
    }
    @Override
    public List<Perfil> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT p FROM Perfil p";
            TypedQuery<Perfil> query = em.createQuery(jpql, Perfil.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
