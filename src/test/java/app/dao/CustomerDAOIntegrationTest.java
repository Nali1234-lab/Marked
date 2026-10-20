package app.dao;

import app.entities.Customer;
import app.entities.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDAOIntegrationTest extends BaseTest {

    private static CustomerDAO customerDAO;

    @BeforeAll
    static void initDAO() {
        // Send Testcontainers-databasen ind i DAO'en
        customerDAO = new CustomerDAO(emf);
    }

    @Test
    void create_shouldPersistCustomer() {
        Customer customer = new Customer("Test Person", "test@integration.dk", "password123", Role.CUSTOMER);

        Customer saved = customerDAO.create(customer);

        assertNotNull(saved.getId(), "Kunden skal have fået et ID fra databasen");
        assertEquals("Test Person", saved.getName());
    }

    @Test
    void getByEmail_shouldReturnCustomer_whenItExists() {
        Customer customer = new Customer("Nali CP", "nali@integration.dk", "hemmelig", Role.ADMIN);
        customerDAO.create(customer);

        Customer found = customerDAO.getByEmail("nali@integration.dk");

        assertNotNull(found);
        assertEquals("Nali CP", found.getName());
        assertEquals("nali@integration.dk", found.getEmail());
    }

    @Test
    void getByEmail_shouldReturnNull_whenItDoesNotExist() {
        Customer found = customerDAO.getByEmail("findesikke@test.dk");
        assertNull(found);
    }
}