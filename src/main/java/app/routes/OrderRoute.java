package app.routes;

import app.api.OrderAPI;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class OrderRoute {

    private final OrderAPI orderController = new OrderAPI();

    protected EndpointGroup getOrderRoutes() {
        return () -> {
            post("/", orderController::createOrder); // Gennemfør køb
            get("/", orderController::getOrders);    // Se mine ordrer
        };
    }
}