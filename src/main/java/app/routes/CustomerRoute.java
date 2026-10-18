package app.routes;

import app.api.CustomerAPI;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CustomerRoute {

    private final CustomerAPI customerController = new CustomerAPI();

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