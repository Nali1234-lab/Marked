package app.routes;

import app.controllers.CatalogController;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class CatalogRoute {

    private final CatalogController catalogController = new CatalogController();

    protected EndpointGroup getProductRoutes() {
        return () -> {
            get("/", catalogController::getAll);
            get("/on-sale", catalogController::getVariantsOnSale); // <-- SKAL stå før {id}
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