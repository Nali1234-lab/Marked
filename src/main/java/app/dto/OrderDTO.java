package app.dto;

import app.entities.Order;
import app.entities.OrderItem;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public record OrderDTO(
        Long orderId,
        LocalDateTime orderDate,
        String status,
        List<OrderItemDTO> items,
        double totalPrice
) {
    public static OrderDTO from(Order order) {
        List<OrderItemDTO> itemDTOs = new ArrayList<>();
        double total = 0.0;

        // Gå igennem alle ordrelinjer
        for (OrderItem item : order.getOrderItems()) {
            OrderItemDTO dto = OrderItemDTO.from(item);
            if (dto != null) {
                itemDTOs.add(dto);
                total = total + dto.lineTotal();
            }
        }

        return new OrderDTO(
                order.getId(),
                order.getOrderDate(),
                order.getStatus().name(),
                itemDTOs,
                total
        );
    }
}