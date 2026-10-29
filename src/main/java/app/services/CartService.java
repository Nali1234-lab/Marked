package app.services;

import app.daos.CartDAO;
import app.daos.CartItemDAO;
import app.daos.ProductConfigurationDAO;
import app.daos.VariantDAO;
import app.dtos.CartDTO;
import app.entities.Cart;
import app.entities.CartItem;
import app.entities.Customer;
import app.entities.ProductConfiguration;
import app.entities.Variant;
import app.exceptions.ApiException;

public class CartService {

    private final CartDAO cartDAO;
    private final CartItemDAO cartItemDAO;
    private final VariantDAO variantDAO;
    private final ProductConfigurationDAO configDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
        this.cartItemDAO = new CartItemDAO();
        this.variantDAO = new VariantDAO();
        this.configDAO = new ProductConfigurationDAO();
    }

    // Hent kurven som en DTO
    public CartDTO getCartContents(Customer customer) {
        Cart cart = getCart(customer); // Genbruger din eksisterende metode
        return CartDTO.from(cart);
    }

    // customer wants to add a variant to their cart
    public void addItemToCart(Customer customer, Long variantId, int qty) {
        Variant variant = variantDAO.getById(variantId);
        if (variant == null) {
            throw new ApiException(404, "Variant not found with id: " + variantId);
        }
        if (variant.getStock() < qty) {
            throw new ApiException(400, "Not enough stock for: " + variant.getName());
        }

        Cart cart = getCart(customer);

        CartItem existing = cart.getCartItems().stream()
                .filter(item -> item.getVariant() != null && item.getVariant().getId().equals(variantId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + qty);
            cartItemDAO.update(existing);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setVariant(variant);
            newItem.setQuantity(qty);
            cartItemDAO.create(newItem);
        }
    }

    // Tilføj en gemt konfiguration til kurven
    public void addConfigurationToCart(Customer customer, Long configurationId, int qty) {
        ProductConfiguration config = configDAO.getById(configurationId);
        if (config == null) {
            throw new ApiException(404, "Konfiguration ikke fundet med id: " + configurationId);
        }

        Cart cart = getCart(customer);

        CartItem existing = cart.getCartItems().stream()
                .filter(item -> item.getConfiguration() != null && item.getConfiguration().getId().equals(configurationId))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + qty);
            cartItemDAO.update(existing);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setConfiguration(config);
            newItem.setQuantity(qty);
            cartItemDAO.create(newItem);
        }
    }

    public void removeItemFromCart(Customer customer, Long variantId) {
        Cart cart = getCart(customer);

        CartItem item = cart.getCartItems().stream()
                .filter(i -> i.getVariant() != null && i.getVariant().getId().equals(variantId))
                .findFirst()
                .orElseThrow(() -> new ApiException(404, "Variant not found in cart"));

        cartItemDAO.delete(item.getId());
    }

    public void clearCart(Customer customer) {
        Cart cart = getCart(customer);
        for (CartItem item : cart.getCartItems()) {
            cartItemDAO.delete(item.getId());
        }
    }

    public Cart getCart(Customer customer) {
        Cart cart = cartDAO.getByCustomerId(customer.getId());
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setCustomer(customer);
            cart = cartDAO.create(newCart);
        }
        return cart;
    }
}