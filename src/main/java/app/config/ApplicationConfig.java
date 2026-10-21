package app.config;

import app.exceptions.ApiException;
import app.routes.Routes;
import app.utils.JwtUtil;
import io.javalin.Javalin;

public class ApplicationConfig {

    private static ApplicationConfig instance;
    private static Javalin app;
    private static Routes routes = new Routes();

    private boolean generalExceptionHandling = false;
    private boolean cors = false;

    private ApplicationConfig() {}

    public static ApplicationConfig getInstance() {
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        return instance;
    }

    public ApplicationConfig initiateServer() {
        return this;
    }

    public ApplicationConfig setGeneralExceptionHandling() {
        generalExceptionHandling = true;
        return this;
    }

    public ApplicationConfig setCORS() {
        cors = true;
        return this;
    }

    public ApplicationConfig startServer(int port) {
        app = Javalin.create(config -> {
            config.router.contextPath = "/api"; // Base path

            // Jackson opsætning til LocalDateTime
            config.jsonMapper(new io.javalin.json.JavalinJackson().updateMapper(mapper -> {
                mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
                mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            }));

            config.router.apiBuilder(routes.getRoutes());
        });

        // 1. Exception handlers
        app.exception(ApiException.class, (e, ctx) -> {
            ctx.status(e.getCode()).json(java.util.Map.of(
                    "status", e.getCode(),
                    "msg", e.getMessage()
            ));
        });

        if (generalExceptionHandling) {
            app.exception(Exception.class, (e, ctx) -> {
                e.printStackTrace();
                ctx.status(500).json(java.util.Map.of(
                        "status", 500,
                        "msg", "Internal server error"
                ));
            });
        }

        // 2. CORS
        if (cors) {
            app.before(ctx -> {
                ctx.header("Access-Control-Allow-Origin", "*");
                ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            });
        }

        // 3. Auth middleware
        app.before(ctx -> {
            String path = ctx.path();

            // Strip "/api" fra stien, så vi kan tjekke uden contextPath
            if (path.startsWith("/api")) {
                path = path.substring("/api".length());
            }

            String method = ctx.method().name();

            // Ignorer OPTIONS (CORS preflight)
            if (method.equals("OPTIONS")) return;

            // Offentlige endpoints
            boolean isPublic =
                    path.equals("/auth/healthcheck") ||
                            path.equals("/auth/register") ||
                            path.equals("/auth/login") ||
                            (path.startsWith("/products") && method.equals("GET")) ||
                            (path.startsWith("/custom-products/") && path.endsWith("/preview") && method.equals("POST"));

            if (isPublic) return;

            // Alt andet kræver et gyldigt JWT-token
            String authHeader = ctx.header("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ApiException(401, "Authorization header mangler eller er ugyldig");
            }
            String token = authHeader.substring("Bearer ".length());

            Long customerId = JwtUtil.getCustomerId(token);
            String role = JwtUtil.getRole(token);

            // NY: Kræv ADMIN-rolle for /admin-stier
            if (path.startsWith("/admin") && !role.equals("ADMIN")) {
                throw new ApiException(403, "Kun administratorer har adgang til dette endpoint");
            }

            ctx.attribute("customerId", customerId);
            ctx.attribute("role", role);
        });

        app.start(port);
        return this;
    }

    public void stopServer() {
        if (app != null) app.stop();
    }

    public Javalin getApp() {
        return app;
    }
}