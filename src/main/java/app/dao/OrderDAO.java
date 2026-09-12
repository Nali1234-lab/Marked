package app.dao;

import app.config.HibernateConfig;
import app.entities.Order;
import app.entities.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class OrderDAO implements IDAO<Order, Long> {

    private final EntityManagerFactory emf;

    public OrderDAO() {
        this.emf = HibernateConfig.getEntityManagerFactory();
    }

    @Override
    public Order create(Order order) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(order);
            em.getTransaction().commit();
            return order;
        }
    }

    @Override
    public List<Order> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT o FROM Order o", Order.class)
                    .getResultList();
        }
    }

    @Override
    public Order getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Order.class, id);
        }
    }

    @Override
    public Order update(Order order) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Order merged = em.merge(order);
            em.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Order order = em.find(Order.class, id);
            if (order != null) {
                em.remove(order);
            }
            em.getTransaction().commit();
        }
    }

    // Matcher: "admin vil kunne se alle betalte ordrer"
    public List<Order> getAllPaid() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT o FROM Order o WHERE o.status = :status", Order.class)
                    .setParameter("status", OrderStatus.PAID)
                    .getResultList();
        }
    }

    // Matcher: "kunde vil kunne se status på sin ordre" (alle en kundes ordrer)
    public List<Order> getByCustomerId(Long customerId) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery(
                            "SELECT o FROM Order o WHERE o.customer.id = :customerId", Order.class)
                    .setParameter("customerId", customerId)
                    .getResultList();
        }
    }
}