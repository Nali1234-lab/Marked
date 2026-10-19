package app.dto;

import app.entities.OrderItem;
import app.entities.ProductConfiguration;
import app.entities.Variant;

public record OrderItemDTO(
        Long itemId,
        String type,        // "VARIANT" eller "CONFIGURATION"
        String name,
        int quantity,
        double unitPrice,
        double lineTotal
) {
    public static OrderItemDTO from(OrderItem item) {
        // Hvis det er en almindelig variant
        if (item.getVariant() != null) {
            Variant v = item.getVariant();
            double price = item.getUnitPriceAtPurchase(); // Brug prisen fra købstidspunktet

            return new OrderItemDTO(
                    item.getId(),
                    "VARIANT",
                    v.getProduct().getName() + " - " + v.getName(),
                    item.getQuantity(),
                    price,
                    price * item.getQuantity()
            );
        }

        // Hvis det er en custom-konfiguration
        if (item.getConfiguration() != null) {
            ProductConfiguration c = item.getConfiguration();
            double price = item.getUnitPriceAtPurchase();

            String options = "";
            for (Variant v : c.getSelectedOptions()) {
                if (options.isEmpty()) {
                    options = v.getName();
                } else {
                    options = options + ", " + v.getName();
                }
            }

            return new OrderItemDTO(
                    item.getId(),
                    "CONFIGURATION",
                    c.getProduct().getName() + " - " + options,
                    item.getQuantity(),
                    price,
                    price * item.getQuantity()
            );
        }

        return null;
    }
}