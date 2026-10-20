package app.dtos;

import app.entities.CartItem;
import app.entities.ProductConfiguration;
import app.entities.Variant;

public record CartItemDTO(
        Long itemId,
        String type,        // "VARIANT" eller "CONFIGURATION"
        String name,
        int quantity,
        double unitPrice,
        double lineTotal
) {
    public static CartItemDTO from(CartItem item) {
        // Hvis det er en almindelig variant
        if (item.getVariant() != null) {
            Variant v = item.getVariant();
            double price = v.getPrice();

            return new CartItemDTO(
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
            double price = c.getFinalPrice();

            // Simpel måde at lave "Rød, Bomuld" på
            String options = "";
            for (Variant v : c.getSelectedOptions()) {
                if (options.isEmpty()) {
                    options = v.getName();
                } else {
                    options = options + ", " + v.getName();
                }
            }

            return new CartItemDTO(
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