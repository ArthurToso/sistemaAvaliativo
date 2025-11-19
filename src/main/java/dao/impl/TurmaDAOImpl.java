package dao.impl;

import dao.TurmaDAO;
import model.Turma;
import util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.hibernate.Hibernate; // Importante para inicializar as listas
import java.util.stream.Collectors;
import model.ProcessoAvaliativo;
import org.hibernate.Hibernate;

import java.util.List;

public class TurmaDAOImpl implements TurmaDAO {

    @Override
    public void salvar(Turma turma) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(turma);

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
    public void atualizar(Turma turma) {
        salvar(turma);
    }

    @Override
    public void deletar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Turma turma = em.find(Turma.class, id);
            if (turma != null) {
                // Antes de remover a turma, precisamos limpar as associações
                // nas tabelas de junção (alunos_turmas, professores_turmas)
                turma.getAlunos().clear();
                turma.getProfessores().clear();

                em.remove(turma);
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
    public Turma buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Turma.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public Turma buscarPorIdComListas(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Turma turma = em.find(Turma.class, id);
            if (turma != null) {
                // Força a inicialização das coleções "lazy"
                // antes de fechar o EntityManager
                Hibernate.initialize(turma.getAlunos());
                Hibernate.initialize(turma.getProfessores());
            }
            return turma;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Turma> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // JPQL para buscar todas as Turmas
            // Usamos 'JOIN FETCH' para já trazer o curso e a UC,
            // evitando consultas N+1 (mais eficiente).
            String jpql = "SELECT t FROM Turma t " +
                    "LEFT JOIN FETCH t.curso " +
                    "LEFT JOIN FETCH t.unidadeCurricular";
            TypedQuery<Turma> query = em.createQuery(jpql, Turma.class);

            // Remove duplicatas que o JOIN FETCH pode causar
            List<Turma> turmas = query.getResultList();
            return turmas.stream().distinct().collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    @Override
    public List<Turma> listarPorProfessor(Long professorId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Busca turmas onde a lista de 'professores' contém o ID passado
            String jpql = "SELECT t FROM Turma t " +
                    "JOIN t.professores p " +
                    "WHERE p.id = :professorId";

            TypedQuery<Turma> query = em.createQuery(jpql, Turma.class);
            query.setParameter("professorId", professorId);
            List<Turma> turmas = query.getResultList();

            for (Turma t : turmas) {
                // Carrega a lista de Processos Avaliativos da Turma
                Hibernate.initialize(t.getProcessosAvaliativos());

                // Para cada Processo, carrega a lista de Formulários
                for (ProcessoAvaliativo pa : t.getProcessosAvaliativos()) {
                    Hibernate.initialize(pa.getFormularios());
                }
            }
            return turmas;
        } finally {
            em.close();
        }
    }

}