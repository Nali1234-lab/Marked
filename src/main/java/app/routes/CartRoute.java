package app.routes;

import app.api.CartAPI;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CartRoute {

    private final CartAPI cartController = new CartAPI();

    protected EndpointGroup getCartRoutes() {
        return () -> {
            post("/items", cartController::addItem);
        };
    }
}