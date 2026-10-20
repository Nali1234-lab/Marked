package app.controllers;

import app.daos.CustomerDAO;
import app.dtos.CustomerDTO;
import app.dtos.LoginRequest;
import app.dtos.LoginResponse;
import app.dtos.RegisterRequest;
import app.entities.Customer;
import app.exceptions.ApiException;
import app.services.AuthService;
import app.utils.JwtUtil;
import io.javalin.http.Context;

public class CustomerController {

    private final AuthService authService = new AuthService();
    private final CustomerDAO customerDAO = new CustomerDAO();

    public void register(Context ctx) {
        RegisterRequest req = ctx.bodyAsClass(RegisterRequest.class);

        if (req.name() == null || req.name().isBlank())
            throw new ApiException(400, "Field 'name' is required");
        if (req.email() == null || req.email().isBlank())
            throw new ApiException(400, "Field 'email' is required");
        if (req.password() == null || req.password().length() < 6)
            throw new ApiException(400, "Field 'password' is required (min 6 tegn)");

        Customer created = authService.register(req.name(), req.email(), req.password());
        ctx.status(201).json(CustomerDTO.from(created));
    }

    public void login(Context ctx) {
        LoginRequest req = ctx.bodyAsClass(LoginRequest.class);

        if (req.email() == null || req.email().isBlank())
            throw new ApiException(400, "Field 'email' is required");
        if (req.password() == null || req.password().isBlank())
            throw new ApiException(400, "Field 'password' is required");

        Customer customer = authService.login(req.email(), req.password());
        String token = JwtUtil.generateToken(customer);

        ctx.json(new LoginResponse(
                token,
                customer.getId(),
                customer.getName(),
                customer.getRole().name()
        ));
    }

    public void getMe(Context ctx) {
        Long customerId = ctx.attribute("customerId");
        if (customerId == null) {
            throw new ApiException(401, "Ikke logget ind");
        }
        Customer customer = customerDAO.getById(customerId);
        if (customer == null) {
            throw new ApiException(404, "Kunde ikke fundet");
        }
        ctx.json(CustomerDTO.from(customer));
    }

    public void getById(Context ctx) {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        Customer customer = customerDAO.getById(id);
        if (customer == null) {
            throw new ApiException(404, "Customer not found with id: " + id);
        }
        ctx.json(CustomerDTO.from(customer));
    }
}