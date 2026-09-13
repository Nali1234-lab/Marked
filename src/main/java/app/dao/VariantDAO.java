package app.dao;

import app.config.HibernateConfig;
import app.entities.Variant;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class VariantDAO implements IDAO<Variant, Long> {

    private final EntityManagerFactory emf;

    public VariantDAO() {
        this.emf = HibernateConfig.getEntityManagerFactory();
    }

    @Override
    public Variant create(Variant variant) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(variant);
            em.getTransaction().commit();
            return variant;
        }
    }

    @Override
    public List<Variant> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT v FROM Variant v", Variant.class)
                    .getResultList();
        }
    }

    @Override
    public Variant getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Variant.class, id);
        }
    }

    @Override
    public Variant update(Variant variant) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Variant merged = em.merge(variant);
            em.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Variant variant = em.find(Variant.class, id);
            if (variant != null) {
                em.remove(variant);
            }
            em.getTransaction().commit();
        }
    }

    // Matcher user story: "kunde vil kunne se varer på tilbud"
    public List<Variant> getOnSale() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT v FROM Variant v WHERE v.onSale = true", Variant.class)
                    .getResultList();
        }
    }
    // Matcher: "kunde vil kunne vælge en variant af det valgte produkt"
    public List<Variant> getByProductId(Long productId) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT v FROM Variant v WHERE v.product.id = :productId", Variant.class)
                    .setParameter("productId", productId)
                    .getResultList();
        }
    }
}