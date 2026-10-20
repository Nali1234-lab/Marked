package app.dtos;

import app.entities.Variant;

public record VariantDTO(Long id, String name, double price, String slot) {

    public static VariantDTO from(Variant v) {
        return new VariantDTO(
                v.getId(),
                v.getName(),
                v.getPrice(),
                v.getDesignSlot() != null ? v.getDesignSlot().name() : "NONE"
        );
    }
}