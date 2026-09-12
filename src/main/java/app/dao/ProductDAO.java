package app.dao;

import app.config.HibernateConfig;
import app.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ProductDAO implements IDAO<Product, Long> {
    // Bemærk: Product er abstrakt, men Hibernate håndterer det fint her.
    // Når vi henter "Product", får vi automatisk de rigtige, konkrete objekter
    // (StandardProduct eller CustomProduct) tilbage - det kaldes polymorfi.

    private final EntityManagerFactory emf;

    public ProductDAO() {
        this.emf = HibernateConfig.getEntityManagerFactory();
    }

    @Override
    public Product create(Product product) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(product);
            em.getTransaction().commit();
            return product;
        }
    }

    @Override
    public List<Product> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT p FROM Product p", Product.class)
                    .getResultList();
        }
    }

    @Override
    public Product getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Product.class, id);
        }
    }

    @Override
    public Product update(Product product) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Product merged = em.merge(product);
            em.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Product product = em.find(Product.class, id);
            if (product != null) {
                em.remove(product);
            }
            em.getTransaction().commit();
        }
    }

    // "kunde vil kunne browse produkter efter kategori"
    public List<Product> getByCategory(Long categoryId) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT p FROM Product p WHERE p.category.id = :categoryId", Product.class)
                    .setParameter("categoryId", categoryId)
                    .getResultList();
        }
    }
}