package app.dto;

import app.entities.Cart;
import app.entities.CartItem;

import java.util.ArrayList;
import java.util.List;

public record CartDTO(
        Long cartId,
        List<CartItemDTO> items,
        double totalPrice
) {
    public static CartDTO from(Cart cart) {
        List<CartItemDTO> itemDTOs = new ArrayList<>();
        double total = 0.0;

        // Gå igennem alle varer i kurven
        for (CartItem item : cart.getCartItems()) {
            CartItemDTO dto = CartItemDTO.from(item);
            if (dto != null) {
                itemDTOs.add(dto);
                total = total + dto.lineTotal();
            }
        }

        return new CartDTO(cart.getId(), itemDTOs, total);
    }
}