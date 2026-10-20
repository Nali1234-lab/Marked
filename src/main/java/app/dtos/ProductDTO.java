package app.dtos;

import app.entities.CustomProduct;
import app.entities.Product;

public record ProductDTO(Long id, String name, double price, String categoryName, String type) {

    public static ProductDTO from(Product p) {
        String type = (p instanceof CustomProduct) ? "CUSTOM" : "STANDARD";
        return new ProductDTO(
                p.getId(),
                p.getName(),
                p.calculatePrice(),
                p.getCategory().getName(),
                type
        );
    }
}