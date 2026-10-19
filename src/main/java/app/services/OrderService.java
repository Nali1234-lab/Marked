package app.services;

import app.dao.OrderDAO;
import app.dao.VariantDAO;
import app.entities.*;
import app.exceptions.ApiException;

import java.time.LocalDateTime;

public class OrderService {

    private final OrderDAO orderDAO;
    private final VariantDAO variantDAO;
    private final CartService cartService;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.variantDAO = new VariantDAO();
        this.cartService = new CartService();
    }

    public Order createOrder(Customer customer, String deliveryStreet, String deliveryZipCode, String deliveryCity) {
        // 1. Hent kundens kurv
        Cart cart = cartService.getCart(customer);

        // 2. Tjek at kurven ikke er tom
        if (cart.getCartItems().isEmpty()) {
            throw new ApiException(400, "Kan ikke oprette en ordre fra en tom kurv");
        }

        // 3. Opret selve ordren
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setDeliveryStreet(deliveryStreet);
        order.setDeliveryZipCode(deliveryZipCode);
        order.setDeliveryCity(deliveryCity);

        // 4. Flyt hver vare fra kurven over i ordren
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQuantity(cartItem.getQuantity());

            // Hvis det er en almindelig variant
            if (cartItem.getVariant() != null) {
                Variant variant = cartItem.getVariant();

                // Tjek lager
                if (variant.getStock() < cartItem.getQuantity()) {
                    throw new ApiException(400, "Ikke nok på lager for: " + variant.getName());
                }

                orderItem.setVariant(variant);
                orderItem.setUnitPriceAtPurchase(variant.getPrice());

                // Træk fra lager
                variant.setStock(variant.getStock() - cartItem.getQuantity());
                variantDAO.update(variant);
            }
            // Hvis det er en custom-konfiguration
            else if (cartItem.getConfiguration() != null) {
                ProductConfiguration config = cartItem.getConfiguration();

                // Tjek lager for BÅDE farve og materiale
                for (Variant option : config.getSelectedOptions()) {
                    if (option.getStock() < cartItem.getQuantity()) {
                        throw new ApiException(400, "Ikke nok på lager for: " + option.getName());
                    }
                }

                orderItem.setConfiguration(config);
                orderItem.setUnitPriceAtPurchase(config.getFinalPrice());

                // Træk fra lager for valgmulighederne
                for (Variant option : config.getSelectedOptions()) {
                    option.setStock(option.getStock() - cartItem.getQuantity());
                    variantDAO.update(option);
                }
            }

            order.getOrderItems().add(orderItem);
        }

        // 5. Gem ordren i databasen
        Order savedOrder = orderDAO.create(order);

        // 6. Tøm kurven, da varerne nu er i en ordre
        cartService.clearCart(customer);

        return savedOrder;
    }

    // "customer wants to see the status of their order"
    public OrderStatus getOrderStatus(Long orderId) {
        Order order = orderDAO.getById(orderId);
        if (order == null) {
            throw new ApiException(404, "Ordre ikke fundet med id: " + orderId);
        }
        return order.getStatus();
    }

    public java.util.List<Order> getCustomerOrders(Long customerId) {
        return orderDAO.getByCustomerId(customerId);
    }
}