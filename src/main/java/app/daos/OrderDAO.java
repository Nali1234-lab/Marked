package app.daos;

import app.config.HibernateConfig;
import app.entities.Order;
import app.entities.OrderItem;
import app.entities.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class OrderDAO implements IDAO<Order, Long> {

    private final EntityManagerFactory emf;

    // Bruges til tests
    public OrderDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

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
            List<Order> orders = em.createQuery("SELECT o FROM Order o", Order.class)
                    .getResultList();
            // Tving Hibernate til at hente relaterede data, før vi lukker databasen
            initializeOrderItems(orders);
            return orders;
        }
    }

    @Override
    public Order getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Order order = em.find(Order.class, id);
            if (order != null) {
                initializeOrderItems(List.of(order));
            }
            return order;
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

    // admin vil kunne se alle betalte ordrer
    public List<Order> getAllPaid() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Order> orders = em.createQuery(
                            "SELECT o FROM Order o WHERE o.status = :status", Order.class)
                    .setParameter("status", OrderStatus.PAID)
                    .getResultList();
            initializeOrderItems(orders);
            return orders;
        }
    }

    // kunde vil kunne se status på sin ordre (alle en kundes ordrer)
    public List<Order> getByCustomerId(Long customerId) {
        try (EntityManager em = emf.createEntityManager()) {
            List<Order> orders = em.createQuery(
                            "SELECT o FROM Order o WHERE o.customer.id = :customerId", Order.class)
                    .setParameter("customerId", customerId)
                    .getResultList();
            initializeOrderItems(orders); // <-- VIGTIGT: Tving data på plads
            return orders;
        }
    }

    // Hjælpemetode: Tving Hibernate til at hente alle relationer
    private void initializeOrderItems(List<Order> orders) {
        for (Order order : orders) {
            for (OrderItem item : order.getOrderItems()) {
                if (item.getVariant() != null) {
                    item.getVariant().getProduct().getName(); // Tving produktdata
                }
                if (item.getConfiguration() != null) {
                    item.getConfiguration().getProduct().getName();
                    item.getConfiguration().getSelectedOptions().size(); // Tving valgte muligheder
                }
            }
        }
    }
}