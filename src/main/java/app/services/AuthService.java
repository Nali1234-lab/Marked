package app.services;

import app.dao.CustomerDAO;
import app.entities.Customer;
import app.entities.Role;
import app.exceptions.ApiException;

public class AuthService {

    private final CustomerDAO customerDAO;

    public AuthService() {
        this.customerDAO = new CustomerDAO();
    }

    // "kunde vil kunne registrere en konto"
    public Customer register(String name, String email, String password) {
        Customer existing = customerDAO.getByEmail(email);
        if (existing != null) {
            throw new ApiException(400, "Email er allerede i brug: " + email);
        }

        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setRole(Role.CUSTOMER);

        return customerDAO.create(customer);
    }

    // "kunde vil kunne logge ind"
    public Customer login(String email, String password) {
        Customer customer = customerDAO.getByEmail(email);
        if (customer == null || !customer.getPassword().equals(password)) {
            throw new ApiException(401, "Forkert email eller password");
        }
        return customer;
    }

    public Customer getCustomerById(Long id) {
        Customer customer = customerDAO.getById(id);
        if (customer == null) {
            throw new ApiException(404, "Kunde ikke fundet med id: " + id);
        }
        return customer;
    }

    public Customer updateCustomer(Customer customer) {
        return customerDAO.update(customer);
    }
}