package app;

import app.api.CatalogAPI;
import app.api.CustomerAPI;
import app.api.HealthAPI;
import app.config.ApplicationConfig;
import app.config.HibernateConfig;

public class App {
    public static void main(String[] args) {
        HibernateConfig.getEntityManagerFactory();

        ApplicationConfig.getInstance()
                .initiateServer()
                .setRoute(new HealthAPI().getRoutes())
                .setRoute(new CustomerAPI().getRoutes())
                .setRoute(new CatalogAPI().getRoutes())   // <-- NY LINJE
                .setGeneralExceptionHandling()
                .setCORS()
                .startServer(7070);
    }
}