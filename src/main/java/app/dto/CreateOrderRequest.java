package app.dto;

public record CreateOrderRequest(
        String deliveryStreet,
        String deliveryZipCode,
        String deliveryCity
) {}