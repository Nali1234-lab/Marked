package app.routes;

import app.controllers.AdminController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class AdminRoute {

    private final AdminController adminController = new AdminController();

    protected EndpointGroup getAdminRoutes() {
        return () -> {
            post("/categories", adminController::createCategory);
            post("/products", adminController::createProduct);
            post("/variants", adminController::createVariant);
            put("/variants/{id}/price", adminController::updatePrice);
            put("/variants/{id}/stock", adminController::updateStock);
            put("/variants/{id}/sale", adminController::updateSale);
            get("/orders/paid", adminController::getPaidOrders);
            put("/orders/{id}/status", adminController::updateOrderStatus);
        };
    }
}