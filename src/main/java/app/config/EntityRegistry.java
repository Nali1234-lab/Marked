package app.config;
import app.entities.*;
import org.hibernate.cfg.Configuration;
final class EntityRegistry {
    private EntityRegistry() {}
    static void registerEntities(Configuration configuration) {
        // TODO: Add more app.entities here...
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(OrderItem.class);
        configuration.addAnnotatedClass(Category.class);
        configuration.addAnnotatedClass(Product.class);
        configuration.addAnnotatedClass(StandardProduct.class);
        configuration.addAnnotatedClass(CustomProduct.class);
        configuration.addAnnotatedClass(Cart.class);
        configuration.addAnnotatedClass(CartItem.class);
        configuration.addAnnotatedClass(Variant.class);
    }
}