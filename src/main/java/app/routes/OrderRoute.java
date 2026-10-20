package app.routes;

import app.controllers.OrderController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class OrderRoute {

    private final OrderController orderController = new OrderController();

    protected EndpointGroup getOrderRoutes() {
        return () -> {
            post("/", orderController::createOrder);
            get("/", orderController::getOrders);
            get("/{id}", orderController::getOrderById); // <-- NY
        };
    }
}