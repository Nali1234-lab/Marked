package app.config;
import entities.Customer;
import org.hibernate.cfg.Configuration;

final class EntityRegistry {

    private EntityRegistry() {}

    static void registerEntities(Configuration configuration) {
      //  configuration.addAnnotatedClass(Study.class);
        // TODO: Add more entities here...
        configuration.addAnnotatedClass(Customer.class);
    }
}