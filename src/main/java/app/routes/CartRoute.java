package app.routes;

import app.controllers.CartController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CartRoute {

    private final CartController cartController = new CartController();

    protected EndpointGroup getCartRoutes() {
        return () -> {
            get("/", cartController::getCart);
            post("/items", cartController::addItem);
            post("/items/variant", cartController::addVariantToCart); // <-- NY
        };
    }
}