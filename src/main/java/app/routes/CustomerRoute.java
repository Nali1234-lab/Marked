package app.routes;

import app.controllers.CustomerController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CustomerRoute {

    private final CustomerController customerController = new CustomerController();

    protected EndpointGroup getAuthRoutes() {
        return () -> {
            post("/register", customerController::register);
            post("/login", customerController::login);
        };
    }

    protected EndpointGroup getCustomerRoutes() {
        return () -> {
            get("/me", customerController::getMe);
            get("/{id}", customerController::getById);
        };
    }
}