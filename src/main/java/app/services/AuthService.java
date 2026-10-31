package app.services;

import app.daos.CustomerDAO;
import app.daos.ISecurityDAO;
import app.entities.Customer;
import app.entities.Role;
import app.exceptions.ApiException;

public class AuthService implements ISecurityDAO {

    private final CustomerDAO customerDAO;

    public AuthService() {
        this.customerDAO = new CustomerDAO();
    }


    @Override
    public Customer register(String name, String email, String password) {
        Customer existing = customerDAO.getByEmail(email);
        if (existing != null) {
            throw new ApiException(400, "Email er allerede i brug: " + email);
        }

        // Her bruger vi vores nye constructor! Hasher automatisk.
        Customer customer = new Customer(name, email, password, Role.CUSTOMER);
        return customerDAO.create(customer);
    }
    @Override
    public Customer login(String email, String password) {
        Customer customer = customerDAO.getByEmail(email);
        if (customer == null || !customer.verifyPassword(password)) {
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


    @Override
    public Customer addUserRole(String email, Role role) {
        Customer customer = customerDAO.getByEmail(email);
        if (customer == null) {
            throw new ApiException(404, "Kunde ikke fundet: " + email);
        }
        customer.addRole(role);
        return customerDAO.update(customer);
    }

    @Override
    public Role createRole(String roleName) {
        try {
            return Role.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Ugyldig rolle: " + roleName + ". Gyldige: CUSTOMER, ADMIN");
        }
    }
}