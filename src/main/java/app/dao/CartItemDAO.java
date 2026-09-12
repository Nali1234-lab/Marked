package app.dao;

import app.config.HibernateConfig;
import app.entities.CartItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class CartItemDAO implements IDAO<CartItem, Long> {
    // Bemærk: fordi Cart har cascade = ALL på sin cartItems-liste,
    // kan man ofte nøjes med at gemme HELE Cart-objektet i stedet
    // for at bruge denne DAO direkte. Den er alligevel nyttig til
    // at slette/opdatere ÉN specifik vare i kurven isoleret.

    private final EntityManagerFactory emf;

    public CartItemDAO() {
        this.emf = HibernateConfig.getEntityManagerFactory();
    }

    @Override
    public CartItem create(CartItem cartItem) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(cartItem);
            em.getTransaction().commit();
            return cartItem;
        }
    }

    @Override
    public List<CartItem> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT ci FROM CartItem ci", CartItem.class)
                    .getResultList();
        }
    }

    @Override
    public CartItem getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(CartItem.class, id);
        }
    }

    @Override
    public CartItem update(CartItem cartItem) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            CartItem merged = em.merge(cartItem);
            em.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            CartItem cartItem = em.find(CartItem.class, id);
            if (cartItem != null) {
                em.remove(cartItem);
            }
            em.getTransaction().commit();
        }
    }
}