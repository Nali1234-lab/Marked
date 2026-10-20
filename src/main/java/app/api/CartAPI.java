package app.api;

import app.dao.CustomerDAO;
import app.dto.AddConfigurationToCartRequest;
import app.dto.AddVariantToCartRequest;
import app.entities.Customer;
import app.exceptions.ApiException;
import app.services.CartService;
import io.javalin.http.Context;

public class CartAPI {

    private final CartService cartService = new CartService();
    private final CustomerDAO customerDAO = new CustomerDAO();

    public void getCart(Context ctx) {
        Long customerId = ctx.attribute("customerId");
        if (customerId == null) throw new ApiException(401, "Du skal være logget ind for at se din kurv");

        Customer customer = customerDAO.getById(customerId);
        if (customer == null) throw new ApiException(404, "Kunde ikke fundet");

        ctx.json(cartService.getCartContents(customer));
    }

    public void addItem(Context ctx) {
        Long customerId = ctx.attribute("customerId");
        if (customerId == null) throw new ApiException(401, "Du skal være logget ind for at lægge i kurven");

        Customer customer = customerDAO.getById(customerId);
        if (customer == null) throw new ApiException(404, "Kunde ikke fundet");

        AddConfigurationToCartRequest req = ctx.bodyAsClass(AddConfigurationToCartRequest.class);
        if (req.configurationId() == null) throw new ApiException(400, "Field 'configurationId' er påkrævet");
        if (req.quantity() <= 0) throw new ApiException(400, "Quantity skal være mindst 1");

        cartService.addConfigurationToCart(customer, req.configurationId(), req.quantity());
        ctx.status(200).json(java.util.Map.of("msg", "Konfiguration tilføjet til kurven"));
    }

    // NY: Læg en almindelig variant (standardprodukt) i kurven
    public void addVariantToCart(Context ctx) {
        Long customerId = ctx.attribute("customerId");
        if (customerId == null) throw new ApiException(401, "Du skal være logget ind for at lægge i kurven");

        Customer customer = customerDAO.getById(customerId);
        if (customer == null) throw new ApiException(404, "Kunde ikke fundet");

        AddVariantToCartRequest req = ctx.bodyAsClass(AddVariantToCartRequest.class);
        if (req.variantId() == null) throw new ApiException(400, "Field 'variantId' er påkrævet");
        if (req.quantity() <= 0) throw new ApiException(400, "Quantity skal være mindst 1");

        cartService.addItemToCart(customer, req.variantId(), req.quantity());
        ctx.status(200).json(java.util.Map.of("msg", "Vare tilføjet til kurven"));
    }
}