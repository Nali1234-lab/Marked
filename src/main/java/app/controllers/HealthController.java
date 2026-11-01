package app.controllers;

import io.javalin.http.Context;

public class HealthController {

    public void healthCheck(Context ctx) {
        ctx.json(java.util.Map.of("status", "ok"));
    }
}