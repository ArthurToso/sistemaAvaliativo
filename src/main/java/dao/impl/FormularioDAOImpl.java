package dao.impl;

import dao.FormularioDAO;
import model.Formulario;
import util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.stream.Collectors;

public class FormularioDAOImpl implements FormularioDAO {

    @Override
    public void salvar(Formulario formulario) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            // Merge é mais seguro para entidades com relações ManyToMany
            em.merge(formulario);
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
    public void atualizar(Formulario formulario) {
        salvar(formulario); // Merge já lida com atualização
    }

    @Override
    public void deletar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Formulario formulario = em.find(Formulario.class, id);
            if (formulario != null) {
                // Limpa associações ManyToMany antes de remover
                formulario.getPerfisDestinados().clear();
                em.remove(formulario);
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
    public Formulario buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Formulario.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Formulario> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Usamos JOIN FETCH para trazer o ProcessoAvaliativo associado
            String jpql = "SELECT f FROM Formulario f " +
                    "LEFT JOIN FETCH f.processoAvaliativo";
            TypedQuery<Formulario> query = em.createQuery(jpql, Formulario.class);

            // Remove duplicatas
            return query.getResultList().stream().distinct().collect(Collectors.toList());
        } finally {
            em.close();
        }
    }
}