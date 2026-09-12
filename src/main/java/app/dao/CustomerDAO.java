package app.dao;

import app.config.HibernateConfig;
import app.entities.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class CustomerDAO implements IDAO<Customer, Long> {

    private final EntityManagerFactory emf;

    public CustomerDAO() {
        this.emf = HibernateConfig.getEntityManagerFactory();
    }

    @Override
    public Customer create(Customer customer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
            return customer;
        }
    }

    @Override
    public List<Customer> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("SELECT c FROM Customer c", Customer.class)
                    .getResultList();
        }
    }

    @Override
    public Customer getById(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Customer.class, id);
        }
    }

    @Override
    public Customer update(Customer customer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Customer merged = em.merge(customer);
            em.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Customer customer = em.find(Customer.class, id);
            if (customer != null) {
                em.remove(customer);
            }
            em.getTransaction().commit();
        }
    }
}