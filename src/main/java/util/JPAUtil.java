package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final String PERSISTENCE_UNIT_NAME = "sistema-avaliativo-PU";

    private static EntityManagerFactory factory;

    // Inicializa a factory uma única vez quando a classe é carregada
    static {
        try {
            factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception e) {
            // Logar o erro ajudaria muito no debug
            System.err.println("Erro ao inicializar EntityManagerFactory: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * Retorna um EntityManager.
     * O EntityManager é o objeto que realmente interage com o banco de dados.
     * Deve ser obtido por transação/requisição.
     */
    public static EntityManager getEntityManager() {
        if (factory == null) {
            throw new IllegalStateException("EntityManagerFactory não foi inicializada.");
        }
        return factory.createEntityManager();
    }

    /**
     * Fecha a factory quando a aplicação é encerrada.
     */
    public static void shutdown() {
        if (factory != null) {
            factory.close();
        }
    }
}