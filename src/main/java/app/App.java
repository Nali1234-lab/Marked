package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;

public class App {
    public static void main(String[] args) {
        HibernateConfig.getEntityManagerFactory();

        ApplicationConfig.getInstance()
                .initiateServer()
                .setGeneralExceptionHandling()
                .setCORS()
                .startServer(7070);
    }
}