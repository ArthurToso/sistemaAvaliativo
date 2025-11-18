package dao.impl;

import dao.FormularioDAO;
import jakarta.persistence.NoResultException;
import model.Formulario;
import model.Questao;
import org.hibernate.Hibernate;
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

    @Override
    public List<Object[]> listarAvaliacoesPendentes(Long alunoId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            /*
             * Esta consulta agora retorna o Formulario (f) E a Turma (t)
             * para a qual a avaliação está disponível.
             */
            String jpql = "SELECT DISTINCT f, t FROM Formulario f " +
                    "JOIN f.processoAvaliativo pa " +
                    "JOIN pa.turmas t " +
                    "JOIN t.alunos a " +
                    "WHERE a.id = :alunoId " +
                    // E ONDE NÃO EXISTIR...
                    "AND NOT EXISTS (" +
                    "  SELECT ar FROM AvaliacaoRespondida ar " +
                    "  WHERE ar.aluno.id = :alunoId " +       // ...uma resposta para este aluno
                    "  AND ar.formulario.id = f.id " +     // ...para este formulário
                    "  AND ar.turma.id = t.id" +             // ...e para esta turma
                    ")";

            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);
            query.setParameter("alunoId", alunoId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Formulario buscarPorIdComQuestoesEAlternativas(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // 1. Busca o formulário e suas questões (1º nível)
            String jpql = "SELECT f FROM Formulario f " +
                    "LEFT JOIN FETCH f.questoes " +
                    "WHERE f.id = :id";
            TypedQuery<Formulario> query = em.createQuery(jpql, Formulario.class);
            query.setParameter("id", id);

            Formulario formulario = query.getSingleResult();

            // 2. Agora, força o carregamento das alternativas (2º nível)
            //    A sessão (em) AINDA ESTÁ ABERTA aqui.
            if (formulario != null) {
                for (Questao q : formulario.getQuestoes()) {
                    Hibernate.initialize(q.getAlternativas());
                }
            }
            return formulario;

        } catch (NoResultException e) {
            return null; // Formulário não encontrado
        } finally {
            em.close(); // Agora sim podemos fechar
        }
    }
}