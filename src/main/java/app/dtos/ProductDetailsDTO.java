package app.dtos;

import app.entities.CustomProduct;
import app.entities.Product;

import java.util.List;

public record ProductDetailsDTO(Long id, String name, double price, String categoryName, String type, List<VariantDTO> designOptions) {

    public static ProductDetailsDTO from(Product p) {
        String type = (p instanceof CustomProduct) ? "CUSTOM" : "STANDARD";
        List<VariantDTO> options = List.of();

        if (p instanceof CustomProduct cp) {
            options = cp.getDesignOptions().stream()
                    .map(VariantDTO::from)
                    .toList();
        }

        return new ProductDetailsDTO(
                p.getId(),
                p.getName(),
                p.calculatePrice(),
                p.getCategory().getName(),
                type,
                options
        );
    }
}