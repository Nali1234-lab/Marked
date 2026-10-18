package app.dto;

import app.entities.Customer;

public record CustomerDTO(Long id, String name, String email, String role) {

    public static CustomerDTO from(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getRole().name()
        );
    }
}