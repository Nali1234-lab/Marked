package app.controllers;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.get;

public class HealthController {

    public EndpointGroup getRoutes() {
        return () -> {
            get("/api/health", ctx -> {
                ctx.json(java.util.Map.of("status", "ok"));
            });
        };
    }
}