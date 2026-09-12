package app;

import app.config.HibernateConfig;
import app.dao.CustomerDAO;
import app.services.AddressApiService;
import app.entities.Customer;
import app.entities.Role;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
       HibernateConfig.getEntityManagerFactory();
        CustomerDAO customerDAO = new CustomerDAO();

        Customer ny = new Customer();
        ny.setName("Test Person");
        ny.setEmail("test@marked.dk");
        ny.setPassword("test123");
        ny.setRole(Role.CUSTOMER);

        customerDAO.create(ny);


        List<Customer> alle = customerDAO.getAll();
        System.out.println("Antal kunder: " + alle.size());
        AddressApiService addressApiService = new AddressApiService();

        boolean gyldig1 = addressApiService.isValidPostalCode("2200", "København N");
        System.out.println("2200 + København N gyldig? " + gyldig1);

        boolean gyldig2 = addressApiService.isValidPostalCode("2200", "Aarhus");
        System.out.println("2200 + Aarhus gyldig? " + gyldig2);

        boolean gyldig3 = addressApiService.isValidPostalCode("9999", "Ingenting");
        System.out.println("9999 + Ingenting gyldig? " + gyldig3);

    }
}
