package app.config;

import io.javalin.Javalin;
import io.javalin.apibuilder.EndpointGroup;

import java.util.ArrayList;
import java.util.List;

public class ApplicationConfig {

    private static ApplicationConfig instance;
    private static Javalin app;

    private final List<EndpointGroup> routeGroups = new ArrayList<>();
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
        // Markerer at serveren skal oprettes. Selve oprettelsen sker i startServer()
        return this;
    }

    public ApplicationConfig setRoute(EndpointGroup group) {
        routeGroups.add(group);
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
        // NU bygges Javalin med alle de opsamlede routes
        app = Javalin.create(config -> {
            config.router.apiBuilder(() -> {
                for (EndpointGroup group : routeGroups) {
                    group.addEndpoints();
                }
            });
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

        if (cors) {
            app.before(ctx -> {
                ctx.header("Access-Control-Allow-Origin", "*");
                ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            });
        }

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