package dao.impl;

import dao.ProcessoAvaliativoDAO;
import model.ProcessoAvaliativo;
import util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

public class ProcessoAvaliativoDAOImpl implements ProcessoAvaliativoDAO {

    @Override
    public void salvar(ProcessoAvaliativo processoAvaliativo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(processoAvaliativo);
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
    public void atualizar(ProcessoAvaliativo processoAvaliativo) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(processoAvaliativo);
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
            ProcessoAvaliativo pa = em.find(ProcessoAvaliativo.class, id);
            if (pa != null) {
                em.remove(pa);
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
    public ProcessoAvaliativo buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(ProcessoAvaliativo.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<ProcessoAvaliativo> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT pa FROM ProcessoAvaliativo pa LEFT JOIN FETCH pa.turmas";
            TypedQuery<ProcessoAvaliativo> query = em.createQuery(jpql, ProcessoAvaliativo.class);
            return query.getResultList().stream().distinct().collect(Collectors.toList());
        } finally {
            em.close();
        }
    }
}