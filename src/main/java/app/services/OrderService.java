package app.services;

import app.daos.OrderDAO;
import app.daos.VariantDAO;
import app.entities.*;
import app.exceptions.ApiException;

import java.time.LocalDateTime;
import java.util.List;

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
        Cart cart = cartService.getCart(customer);
        if (cart.getCartItems().isEmpty()) {
            throw new ApiException(400, "Kan ikke oprette en ordre fra en tom kurv");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setDeliveryStreet(deliveryStreet);
        order.setDeliveryZipCode(deliveryZipCode);
        order.setDeliveryCity(deliveryCity);

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQuantity(cartItem.getQuantity());

            if (cartItem.getVariant() != null) {
                Variant variant = cartItem.getVariant();
                if (variant.getStock() < cartItem.getQuantity()) {
                    throw new ApiException(400, "Ikke nok på lager for: " + variant.getName());
                }
                orderItem.setVariant(variant);
                orderItem.setUnitPriceAtPurchase(variant.getPrice());
                variant.setStock(variant.getStock() - cartItem.getQuantity());
                variantDAO.update(variant);
            } else if (cartItem.getConfiguration() != null) {
                ProductConfiguration config = cartItem.getConfiguration();
                for (Variant option : config.getSelectedOptions()) {
                    if (option.getStock() < cartItem.getQuantity()) {
                        throw new ApiException(400, "Ikke nok på lager for: " + option.getName());
                    }
                }
                orderItem.setConfiguration(config);
                orderItem.setUnitPriceAtPurchase(config.getFinalPrice());
                for (Variant option : config.getSelectedOptions()) {
                    option.setStock(option.getStock() - cartItem.getQuantity());
                    variantDAO.update(option);
                }
            }

            order.getOrderItems().add(orderItem);
        }

        Order savedOrder = orderDAO.create(order);
        cartService.clearCart(customer);
        return savedOrder;
    }

    public OrderStatus getOrderStatus(Long orderId) {
        Order order = orderDAO.getById(orderId);
        if (order == null) {
            throw new ApiException(404, "Ordre ikke fundet med id: " + orderId);
        }
        return order.getStatus();
    }

    public List<Order> getCustomerOrders(Long customerId) {
        return orderDAO.getByCustomerId(customerId);
    }

    public List<Order> getAllPaidOrders() {
        return orderDAO.getAllPaid();
    }

    public void updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderDAO.getById(orderId);
        if (order == null) {
            throw new ApiException(404, "Ordre ikke fundet med id: " + orderId);
        }
        order.setStatus(newStatus);
        orderDAO.update(order);
    }

    public Order getOrderById(Long orderId) {
        Order order = orderDAO.getById(orderId);
        if (order == null) {
            throw new ApiException(404, "Ordre ikke fundet med id: " + orderId);
        }
        return order;
    }
}