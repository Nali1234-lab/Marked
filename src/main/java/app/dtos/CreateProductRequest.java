package app.dtos;

public record CreateProductRequest(
        String name,
        String type,        // "STANDARD" eller "CUSTOM"
        double price,       // Kun for STANDARD
        double laborCost,   // Kun for CUSTOM
        double hiddenMargin,// Kun for CUSTOM
        Long categoryId
) {}