package app.dtos;

public record LoginResponse(String token, Long customerId, String name, String role) {}