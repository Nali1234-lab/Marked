package app.dao;

import app.config.HibernateConfig;
import app.entities.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class CategoryDAO implements IDAO<Category, Long> {

    private final EntityManagerFactory emf;

    public CategoryDAO() {
        this.emf = HibernateConfig.getEntityManagerFactory();
    }

    @Override
    public Category create(Category category) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(category);
            em.getTransaction().commit();
            return category;
        }
    }

    @Override
    public List<Category> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT c FROM Category c", Category.class)
                    .getResultList();
        }
    }

    @Override
    public Category getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Category.class, id);
        }
    }

    @Override
    public Category update(Category category) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Category merged = em.merge(category);
            em.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Category category = em.find(Category.class, id);
            if (category != null) {
                em.remove(category);
            }
            em.getTransaction().commit();
        }
    }
}