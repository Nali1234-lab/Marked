package app.dao;

import app.config.HibernateConfig;
import app.entities.OrderItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class OrderItemDAO implements IDAO<OrderItem, Long> {

    private final EntityManagerFactory emf;

    public OrderItemDAO() {
        this.emf = HibernateConfig.getEntityManagerFactory();
    }

    @Override
    public OrderItem create(OrderItem orderItem) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(orderItem);
            em.getTransaction().commit();
            return orderItem;
        }
    }

    @Override
    public List<OrderItem> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT oi FROM OrderItem oi", OrderItem.class)
                    .getResultList();
        }
    }

    @Override
    public OrderItem getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(OrderItem.class, id);
        }
    }

    @Override
    public OrderItem update(OrderItem orderItem) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            OrderItem merged = em.merge(orderItem);
            em.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            OrderItem orderItem = em.find(OrderItem.class, id);
            if (orderItem != null) {
                em.remove(orderItem);
            }
            em.getTransaction().commit();
        }
    }
}