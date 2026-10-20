package app.dtos;

public record CreateOrderRequest(
        String deliveryStreet,
        String deliveryZipCode,
        String deliveryCity
) {}