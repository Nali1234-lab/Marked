package app.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final CustomerRoute customerRoute = new CustomerRoute();
    private final CatalogRoute catalogRoute = new CatalogRoute();
    private final CartRoute cartRoute = new CartRoute();

    public EndpointGroup getRoutes() {
        return () -> {
            // Sundhedstjek (offentlig)
            get("/health", ctx -> ctx.json(java.util.Map.of("status", "ok")));

            // Ruterne
            path("/auth", customerRoute.getAuthRoutes());
            path("/customers", customerRoute.getCustomerRoutes());
            path("/products", catalogRoute.getProductRoutes());
            path("/custom-products", catalogRoute.getCustomProductRoutes());
            path("/cart", cartRoute.getCartRoutes());
        };
    }
}