package app.services;

import app.dao.CartDAO;
import app.dao.CartItemDAO;
import app.dao.VariantDAO;
import app.entities.Cart;
import app.entities.CartItem;
import app.entities.Customer;
import app.entities.Variant;
import app.exceptions.ApiException;

public class CartService {

    private final CartDAO cartDAO;
    private final CartItemDAO cartItemDAO;
    private final VariantDAO variantDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
        this.cartItemDAO = new CartItemDAO();
        this.variantDAO = new VariantDAO();
    }

    // "customer wants to add a variant to their cart"
    public void addItemToCart(Customer customer, Long variantId, int qty) {
        Variant variant = variantDAO.getById(variantId);
        if (variant == null) {
            throw new ApiException(404, "Variant not found with id: " + variantId);
        }
        if (variant.getStock() < qty) {
            throw new ApiException(400, "Not enough stock for: " + variant.getName());
        }

        Cart cart = getCart(customer);

        // Does the customer already have this variant in their cart? Increase quantity instead of duplicating.
        CartItem existing = cart.getCartItems().stream()
                .filter(item -> item.getVariant().getId().equals(variantId))
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

    // "customer wants to remove an item from their cart"
    public void removeItemFromCart(Customer customer, Long variantId) {
        Cart cart = getCart(customer);

        CartItem item = cart.getCartItems().stream()
                .filter(i -> i.getVariant().getId().equals(variantId))
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

    // Fetches the customer's cart - creates an empty one automatically if none exists yet
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