package app.dao;

import app.config.HibernateConfig;
import app.entities.CustomProduct;
import app.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class ProductDAO implements IDAO<Product, Long> {

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
            List<Product> products = em.createQuery("SELECT p FROM Product p", Product.class)
                    .getResultList();
            // Tving Hibernate til at hente relationer, før EntityManager lukkes
            for (Product p : products) {
                p.getCategory().getName();
                if (p instanceof CustomProduct cp) {
                    cp.getDesignOptions().size();
                }
            }
            return products;
        }
    }

    @Override
    public Product getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Product p = em.find(Product.class, id);
            if (p != null) {
                p.getCategory().getName();
                if (p instanceof CustomProduct cp) {
                    cp.getDesignOptions().size();
                }
            }
            return p;
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

    public List<Product> getByCategory(Long categoryId) {
        try (EntityManager em = emf.createEntityManager()) {
            List<Product> products = em.createQuery(
                            "SELECT p FROM Product p WHERE p.category.id = :categoryId", Product.class)
                    .setParameter("categoryId", categoryId)
                    .getResultList();
            for (Product p : products) {
                p.getCategory().getName();
                if (p instanceof CustomProduct cp) {
                    cp.getDesignOptions().size();
                }
            }
            return products;
        }
    }
}