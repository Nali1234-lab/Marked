package app.api;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;

public class HealthAPI {

    public EndpointGroup getRoutes() {
        return () -> {
            get("/api/health", ctx -> {
                ctx.json(java.util.Map.of("status", "ok"));
            });
        };
    }
}