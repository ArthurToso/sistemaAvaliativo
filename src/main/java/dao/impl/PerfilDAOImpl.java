package dao.impl;

import dao.PerfilDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Perfil;
import util.JPAUtil;

import java.util.List;

public class PerfilDAOImpl implements PerfilDAO {
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
