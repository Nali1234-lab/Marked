package app.daos;

import app.entities.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Properties;

@Testcontainers
public abstract class BaseTest {

    @Container
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16.2")
                    .withDatabaseName("test_marked_db")
                    .withUsername("test")
                    .withPassword("test");

    protected static EntityManagerFactory emf;

    @BeforeAll
    static void setUpAll() {
        Properties props = new Properties();
        props.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        props.put("hibernate.connection.url", postgres.getJdbcUrl());
        props.put("hibernate.connection.username", postgres.getUsername());
        props.put("hibernate.connection.password", postgres.getPassword());
        props.put("hibernate.hbm2ddl.auto", "create-drop");

        Configuration configuration = new Configuration();
        configuration.setProperties(props);

        // Registrer alle dine entities
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(OrderItem.class);
        configuration.addAnnotatedClass(Category.class);
        configuration.addAnnotatedClass(Product.class);
        configuration.addAnnotatedClass(StandardProduct.class);
        configuration.addAnnotatedClass(CustomProduct.class);
        configuration.addAnnotatedClass(Cart.class);
        configuration.addAnnotatedClass(CartItem.class);
        configuration.addAnnotatedClass(Variant.class);
        configuration.addAnnotatedClass(ProductConfiguration.class);

        emf = configuration.buildSessionFactory().unwrap(EntityManagerFactory.class);
    }

    @AfterAll
    static void tearDownAll() {
        if (emf != null) emf.close();
    }

    @BeforeEach
    void cleanDatabase() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            // Slet i den rækkefølge, der respekterer foreign keys
            em.createQuery("DELETE FROM OrderItem").executeUpdate();
            em.createQuery("DELETE FROM CartItem").executeUpdate();
            em.createQuery("DELETE FROM ProductConfiguration").executeUpdate();
            em.createQuery("DELETE FROM Order").executeUpdate();
            em.createQuery("DELETE FROM Cart").executeUpdate();
            em.createQuery("DELETE FROM Customer").executeUpdate();
            em.createQuery("DELETE FROM Variant").executeUpdate();
            em.createQuery("DELETE FROM StandardProduct").executeUpdate();
            em.createQuery("DELETE FROM CustomProduct").executeUpdate();
            em.createQuery("DELETE FROM Product").executeUpdate();
            em.createQuery("DELETE FROM Category").executeUpdate();
            em.getTransaction().commit();
        }
    }
}