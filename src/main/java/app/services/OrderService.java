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

    // "customer wants to complete a purchase, so their order is created"
    public Order createOrder(Customer customer, String deliveryStreet, String deliveryZipCode, String deliveryCity) {
        Cart cart = cartService.getCart(customer);

        if (cart.getCartItems().isEmpty()) {
            throw new ApiException(400, "Cannot create an order from an empty cart");
        }

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setDeliveryStreet(deliveryStreet);
        order.setDeliveryZipCode(deliveryZipCode);
        order.setDeliveryCity(deliveryCity);

        for (CartItem cartItem : cart.getCartItems()) {
            Variant variant = cartItem.getVariant();

            if (variant.getStock() < cartItem.getQuantity()) {
                throw new ApiException(400, "Not enough stock for: " + variant.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setVariant(variant);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPriceAtPurchase(variant.getPrice());
            order.getOrderItems().add(orderItem);

            // Reduce stock now - reserved for this order, not just sitting in a cart.
            variant.setStock(variant.getStock() - cartItem.getQuantity());
            variantDAO.update(variant);
        }

        Order savedOrder = orderDAO.create(order);
        cartService.clearCart(customer);

        return savedOrder;
    }

    // "customer wants to complete payment, so their order becomes 'paid'"
    public boolean processPayment(Order order) {
        if (!validatePayment(order)) {
            throw new ApiException(402, "Payment could not be validated");
        }

        order.setStatus(OrderStatus.PAID);
        orderDAO.update(order);
        return true;
    }

    // Stub for now - in a real system this would talk to a payment provider.
    public boolean validatePayment(Order order) {
        return order != null && order.getStatus() == OrderStatus.PENDING;
    }

    // "customer wants to see the status of their order"
    public OrderStatus getOrderStatus(Long orderId) {
        Order order = orderDAO.getById(orderId);
        if (order == null) {
            throw new ApiException(404, "Order not found with id: " + orderId);
        }
        return order.getStatus();
    }

    public java.util.List<Order> getCustomerOrders(Long customerId) {
        return orderDAO.getByCustomerId(customerId);
    }

    // "admin wants to see all paid orders, to know what needs packing"
    public java.util.List<Order> getAllPaidOrders() {
        return orderDAO.getAllPaid();
    }

    // "admin wants to update an order's status (e.g. to DONE after shipping)"
    public void updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderDAO.getById(orderId);
        if (order == null) {
            throw new ApiException(404, "Order not found with id: " + orderId);
        }
        order.setStatus(newStatus);
        orderDAO.update(order);
    }
}