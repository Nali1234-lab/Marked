package app.controllers;

import app.dtos.*;
import app.entities.*;
import app.exceptions.ApiException;
import app.services.CatalogService;
import app.services.OrderService;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class AdminController {

    private final CatalogService catalogService = new CatalogService();
    private final OrderService orderService = new OrderService();

    public void createCategory(Context ctx) {
        CreateCategoryRequest req = ctx.bodyAsClass(CreateCategoryRequest.class);
        if (req.name() == null || req.name().isBlank()) throw new ApiException(400, "Field 'name' er påkrævet");
        Category created = catalogService.createCategory(req.name());
        ctx.status(201).json(CategoryDTO.from(created));
    }

    public void createProduct(Context ctx) {
        CreateProductRequest req = ctx.bodyAsClass(CreateProductRequest.class);
        if (req.name() == null || req.name().isBlank()) throw new ApiException(400, "Field 'name' er påkrævet");
        if (req.type() == null || req.type().isBlank()) throw new ApiException(400, "Field 'type' er påkrævet");
        if (req.categoryId() == null) throw new ApiException(400, "Field 'categoryId' er påkrævet");

        Product created = catalogService.createProduct(req);
        ctx.status(201).json(ProductDTO.from(created));
    }

    public void createVariant(Context ctx) {
        CreateVariantRequest req = ctx.bodyAsClass(CreateVariantRequest.class);
        if (req.name() == null || req.name().isBlank()) throw new ApiException(400, "Field 'name' er påkrævet");
        if (req.productId() == null) throw new ApiException(400, "Field 'productId' er påkrævet");

        DesignSlot slot = null;
        if (req.designSlot() != null && !req.designSlot().isBlank()) {
            try {
                slot = DesignSlot.valueOf(req.designSlot().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ApiException(400, "Ugyldig designSlot. Brug COLOR eller MATERIAL");
            }
        }

        Variant created = catalogService.createVariant(
                req.productId(), req.name(), req.price(), req.stock(), slot
        );

        ctx.status(201).json(VariantResponseDTO.from(created));
    }

    public void updatePrice(Context ctx) {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        UpdatePriceRequest req = ctx.bodyAsClass(UpdatePriceRequest.class);
        if (req.price() < 0) throw new ApiException(400, "Prisen kan ikke være negativ");
        catalogService.updatePrice(id, req.price());
        ctx.json(java.util.Map.of("msg", "Pris opdateret"));
    }

    public void updateStock(Context ctx) {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        UpdateStockRequest req = ctx.bodyAsClass(UpdateStockRequest.class);
        if (req.stock() < 0) throw new ApiException(400, "Lageret kan ikke være negativt");
        catalogService.updateStock(id, req.stock());
        ctx.json(java.util.Map.of("msg", "Lager opdateret"));
    }

    public void updateSale(Context ctx) {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        UpdateSaleRequest req = ctx.bodyAsClass(UpdateSaleRequest.class);
        catalogService.setOnSale(id, req.onSale());
        String besked = req.onSale() ? "Variant sat på tilbud" : "Variant fjernet fra tilbud";
        ctx.json(java.util.Map.of("msg", besked));
    }

    // NY: Se alle betalte ordrer
    public void getPaidOrders(Context ctx) {
        List<Order> orders = orderService.getAllPaidOrders();
        List<OrderDTO> dtos = new ArrayList<>();
        for (Order order : orders) {
            dtos.add(OrderDTO.from(order));
        }
        ctx.json(dtos);
    }

    // NY: Opdater en ordres status
    public void updateOrderStatus(Context ctx) {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        UpdateOrderStatusRequest req = ctx.bodyAsClass(UpdateOrderStatusRequest.class);

        if (req.status() == null || req.status().isBlank()) {
            throw new ApiException(400, "Field 'status' er påkrævet (PENDING, PAID eller DONE)");
        }

        OrderStatus newStatus;
        try {
            newStatus = OrderStatus.valueOf(req.status().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Ugyldig status. Brug PENDING, PAID eller DONE");
        }

        orderService.updateStatus(id, newStatus);
        ctx.json(java.util.Map.of("msg", "Ordrestatus opdateret til " + newStatus));
    }
}