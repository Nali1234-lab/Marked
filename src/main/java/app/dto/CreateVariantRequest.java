package app.dto;

public record CreateVariantRequest(
        String name,
        double price,
        int stock,
        String designSlot, // "COLOR", "MATERIAL" eller null (for standard)
        Long productId
) {}