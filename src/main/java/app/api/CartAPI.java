package app.api;

import app.dao.CustomerDAO;
import app.dto.AddConfigurationToCartRequest;
import app.dto.CartDTO;
import app.entities.Customer;
import app.exceptions.ApiException;
import app.services.CartService;
import io.javalin.http.Context;

public class CartAPI {

    private final CartService cartService = new CartService();
    private final CustomerDAO customerDAO = new CustomerDAO();

    // NY: Se indholdet af kurven
    public void getCart(Context ctx) {
        // 1. Hent kundens ID (lagt ind af middleware)
        Long customerId = ctx.attribute("customerId");
        if (customerId == null) {
            throw new ApiException(401, "Du skal være logget ind for at se din kurv");
        }

        // 2. Find kunden i databasen
        Customer customer = customerDAO.getById(customerId);
        if (customer == null) {
            throw new ApiException(404, "Kunde ikke fundet");
        }

        // 3. Kald servicen og få en CartDTO tilbage
        CartDTO cart = cartService.getCartContents(customer);

        // 4. Send JSON-svaret
        ctx.json(cart);
    }

    // Eksisterende metode
    public void addItem(Context ctx) {
        Long customerId = ctx.attribute("customerId");
        if (customerId == null) {
            throw new ApiException(401, "Du skal være logget ind for at lægge i kurven");
        }

        Customer customer = customerDAO.getById(customerId);
        if (customer == null) {
            throw new ApiException(404, "Kunde ikke fundet");
        }

        AddConfigurationToCartRequest req = ctx.bodyAsClass(AddConfigurationToCartRequest.class);

        if (req.configurationId() == null) {
            throw new ApiException(400, "Field 'configurationId' er påkrævet");
        }
        if (req.quantity() <= 0) {
            throw new ApiException(400, "Quantity skal være mindst 1");
        }

        cartService.addConfigurationToCart(customer, req.configurationId(), req.quantity());

        ctx.status(200).json(java.util.Map.of("msg", "Konfiguration tilføjet til kurven"));
    }
}