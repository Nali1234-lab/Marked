package app.routes;

import app.api.CatalogAPI;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CatalogRoute {

    private final CatalogAPI catalogController = new CatalogAPI();

    protected EndpointGroup getProductRoutes() {
        return () -> {
            get("/", catalogController::getAll);
            get("/{id}", catalogController::getById);
        };
    }

    protected EndpointGroup getCustomProductRoutes() {
        return () -> {
            post("/{id}/preview", catalogController::preview);
            post("/{id}/configure", catalogController::configure);
        };
    }
}