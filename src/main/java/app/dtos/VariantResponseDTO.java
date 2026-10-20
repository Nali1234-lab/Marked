package app.dtos;

import app.entities.Variant;

public record VariantResponseDTO(
        Long id,
        String name,
        double price,
        int stock,
        boolean onSale,
        String designSlot,
        Long productId
) {
    public static VariantResponseDTO from(Variant variant) {
        return new VariantResponseDTO(
                variant.getId(),
                variant.getName(),
                variant.getPrice(),
                variant.getStock(),
                variant.isOnSale(),
                variant.getDesignSlot() != null ? variant.getDesignSlot().name() : null,
                variant.getProduct().getId()
        );
    }
}