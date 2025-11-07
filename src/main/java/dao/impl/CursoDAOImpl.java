package dao.impl;

import dao.CursoDAO;
import model.Curso;
import util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Implementação da interface CursoDAO usando JPA.
 */
public class CursoDAOImpl implements CursoDAO {

    @Override
    public void salvar(Curso curso) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(curso);
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
    public void atualizar(Curso curso) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(curso); // 'merge' é usado para atualizar uma entidade
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
            Curso curso = em.find(Curso.class, id); // Busca o curso
            if (curso != null) {
                em.remove(curso); // Remove o curso
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
    public Curso buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Curso.class, id); // 'find' é a forma mais simples de buscar por ID
        } finally {
            em.close();
        }
    }

    @Override
    public List<Curso> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // JPQL (Java Persistence Query Language) para buscar todos os objetos Curso
            String jpql = "SELECT c FROM Curso c";
            TypedQuery<Curso> query = em.createQuery(jpql, Curso.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}