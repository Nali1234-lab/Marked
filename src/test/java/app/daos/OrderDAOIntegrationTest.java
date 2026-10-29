package app.daos;

import app.entities.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderDAOIntegrationTest extends BaseTest {

    private static OrderDAO orderDAO;
    private static CustomerDAO customerDAO;

    @BeforeAll
    static void initDAO() {
        orderDAO = new OrderDAO(emf);
        customerDAO = new CustomerDAO(emf);
    }

    @Test
    void getByCustomerId_shouldReturnOrdersForSpecificCustomer() {
        Customer customer = new Customer("Kunde Et", "kunde1@test.dk", "password", Role.CUSTOMER);
        customerDAO.create(customer);

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setDeliveryStreet("Testvej 1");
        order.setDeliveryZipCode("1234");
        order.setDeliveryCity("Byen");
        orderDAO.create(order);

        List<Order> foundOrders = orderDAO.getByCustomerId(customer.getId());

        assertEquals(1, foundOrders.size());
        assertEquals(customer.getId(), foundOrders.get(0).getCustomer().getId());
        assertEquals(OrderStatus.PENDING, foundOrders.get(0).getStatus());
    }
}