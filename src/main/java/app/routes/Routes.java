package app.routes;

import app.controllers.HealthController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final CustomerRoute customerRoute = new CustomerRoute();
    private final CatalogRoute catalogRoute = new CatalogRoute();
    private final CartRoute cartRoute = new CartRoute();
    private final OrderRoute orderRoute = new OrderRoute();
    private final AdminRoute adminRoute = new AdminRoute(); // <-- NY

    public EndpointGroup getRoutes() {
        return () -> {
            // Opret instansen øverst i klassen (hvis den ikke er der)
            final HealthController healthController = new HealthController();

// Inde i getRoutes():
            get("/health", healthController::healthCheck);

            // Ruterne
            path("/auth", customerRoute.getAuthRoutes());
            path("/customers", customerRoute.getCustomerRoutes());
            path("/products", catalogRoute.getProductRoutes());
            path("/custom-products", catalogRoute.getCustomProductRoutes());
            path("/cart", cartRoute.getCartRoutes());
            path("/orders", orderRoute.getOrderRoutes());

            // Admin ruter
            path("/admin", adminRoute.getAdminRoutes());
        };
    }
}