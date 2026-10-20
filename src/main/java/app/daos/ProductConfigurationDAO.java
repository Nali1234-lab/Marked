package app.daos;

import app.config.HibernateConfig;
import app.entities.ProductConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class ProductConfigurationDAO {

    private final EntityManagerFactory emf;

    public ProductConfigurationDAO() {
        this.emf = HibernateConfig.getEntityManagerFactory();
    }

    public ProductConfiguration create(ProductConfiguration config) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(config);
            em.getTransaction().commit();
            return config;
        }
    }

    public ProductConfiguration getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            ProductConfiguration config = em.find(ProductConfiguration.class, id);
            if (config != null) {
                // Tving Hibernate til at hente de valgte varianter
                config.getSelectedOptions().size();
            }
            return config;
        }
    }
}