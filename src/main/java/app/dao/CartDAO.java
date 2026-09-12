package app.dao;

import app.config.HibernateConfig;
import app.entities.Cart;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class CartDAO implements IDAO<Cart, Long> {

    private final EntityManagerFactory emf;

    public CartDAO() {
        this.emf = HibernateConfig.getEntityManagerFactory();
    }

    @Override
    public Cart create(Cart cart) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(cart);
            em.getTransaction().commit();
            return cart;
        }
    }

    @Override
    public List<Cart> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT c FROM Cart c", Cart.class)
                    .getResultList();
        }
    }

    @Override
    public Cart getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Cart.class, id);
        }
    }

    @Override
    public Cart update(Cart cart) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Cart merged = em.merge(cart);
            em.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Cart cart = em.find(Cart.class, id);
            if (cart != null) {
                em.remove(cart);
            }
            em.getTransaction().commit();
        }
    }

    // Matcher: "kunde logger ind -> skal have adgang til sin kurv"
    public Cart getByCustomerId(Long customerId) {
        try (EntityManager em = emf.createEntityManager()) {
            List<Cart> result = em.createQuery(
                            "SELECT c FROM Cart c WHERE c.customer.id = :customerId", Cart.class)
                    .setParameter("customerId", customerId)
                    .getResultList();
            return result.isEmpty() ? null : result.get(0);
        }
    }
}