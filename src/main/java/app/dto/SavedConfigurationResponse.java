package app.dto;

import java.util.List;

public record SavedConfigurationResponse(
        Long configurationId,
        String productName,
        List<String> selectedOptions,
        double finalPrice,
        int quantity
) {}