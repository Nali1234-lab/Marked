package app.services;

import app.config.HibernateConfig;
import app.entities.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;

// den klasse er kun til at teste admin og custommer rile virke eller ej: jeg sletter den senere
public class AuthService {
    public Customer login(String email, String password) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        EntityManager em = emf.createEntityManager();

        try {
            Customer customer = em.createQuery(
                            "SELECT c FROM Customer c WHERE c.email = :email", Customer.class)
                    .setParameter("email", email)
                    .getSingleResult();

            if (!customer.getPassword().equals(password)) {
                throw new RuntimeException("Forkert password");
            }

            return customer;
        } catch (NoResultException e) {
            throw new RuntimeException("Ingen bruger fundet med den email");
        } finally {
            em.close();
        }
    }
}
