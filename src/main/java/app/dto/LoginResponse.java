package app.dto;

public record LoginResponse(String token, Long customerId, String name, String role) {}