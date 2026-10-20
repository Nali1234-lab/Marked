package app.routes;

import app.api.CartAPI;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CartRoute {

    private final CartAPI cartController = new CartAPI();

    protected EndpointGroup getCartRoutes() {
        return () -> {
            get("/", cartController::getCart);
            post("/items", cartController::addItem);
            post("/items/variant", cartController::addVariantToCart); // <-- NY
        };
    }
}