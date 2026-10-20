package app.api;

import app.dao.CustomerDAO;
import app.dto.CreateOrderRequest;
import app.dto.OrderDTO;
import app.entities.Customer;
import app.entities.Order;
import app.exceptions.ApiException;
import app.services.OrderService;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class OrderAPI {

    private final OrderService orderService = new OrderService();
    private final CustomerDAO customerDAO = new CustomerDAO();

    public void createOrder(Context ctx) {
        Long customerId = ctx.attribute("customerId");
        if (customerId == null) throw new ApiException(401, "Du skal være logget ind for at gennemføre et køb");

        Customer customer = customerDAO.getById(customerId);
        if (customer == null) throw new ApiException(404, "Kunde ikke fundet");

        CreateOrderRequest req = ctx.bodyAsClass(CreateOrderRequest.class);
        if (req.deliveryStreet() == null || req.deliveryStreet().isBlank()) throw new ApiException(400, "Field 'deliveryStreet' er påkrævet");
        if (req.deliveryZipCode() == null || req.deliveryZipCode().isBlank()) throw new ApiException(400, "Field 'deliveryZipCode' er påkrævet");
        if (req.deliveryCity() == null || req.deliveryCity().isBlank()) throw new ApiException(400, "Field 'deliveryCity' er påkrævet");

        Order order = orderService.createOrder(customer, req.deliveryStreet(), req.deliveryZipCode(), req.deliveryCity());
        ctx.status(201).json(OrderDTO.from(order));
    }

    public void getOrders(Context ctx) {
        Long customerId = ctx.attribute("customerId");
        if (customerId == null) throw new ApiException(401, "Du skal være logget ind for at se dine ordrer");

        List<Order> orders = orderService.getCustomerOrders(customerId);
        List<OrderDTO> dtos = new ArrayList<>();
        for (Order order : orders) {
            dtos.add(OrderDTO.from(order));
        }
        ctx.json(dtos);
    }

    // NY: Se én specifik ordre
    public void getOrderById(Context ctx) {
        Long customerId = ctx.attribute("customerId");
        if (customerId == null) throw new ApiException(401, "Du skal være logget ind");

        long orderId = ctx.pathParamAsClass("id", Long.class).get();
        Order order = orderService.getOrderById(orderId);

        // Sikkerhed: Kun ejeren af ordren må se den
        if (!order.getCustomer().getId().equals(customerId)) {
            throw new ApiException(403, "Du har ikke adgang til denne ordre");
        }

        ctx.json(OrderDTO.from(order));
    }
}