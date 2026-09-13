package app.services;

import app.dao.CategoryDAO;
import app.dao.ProductDAO;
import app.dao.VariantDAO;
import app.entities.*;
import app.exceptions.ApiException;

import java.util.List;

public class CatalogService {

    private final CategoryDAO categoryDAO;
    private final ProductDAO productDAO;
    private final VariantDAO variantDAO;

    public CatalogService() {
        this.categoryDAO = new CategoryDAO();
        this.productDAO = new ProductDAO();
        this.variantDAO = new VariantDAO();
    }

    // ===== CATEGORY =====

    public List<Category> getAllCategories() {
        return categoryDAO.getAll();
    }

    // "admin wants to create a new category"
    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryDAO.create(category);
    }

    // "admin wants to delete a category that is no longer used"
    public void deleteCategory(Long categoryId) {
        List<Product> products = productDAO.getByCategory(categoryId);
        if (!products.isEmpty()) {
            throw new ApiException(400, "Cannot delete a category that still has products");
        }
        categoryDAO.delete(categoryId);
    }

    // ===== PRODUCT =====

    // "customer wants to browse products by category"
    public List<Product> getProductsByCategory(Long categoryId) {
        return productDAO.getByCategory(categoryId);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAll();
    }

    // "admin wants to add a new standard product to an existing category"
    public StandardProduct createStandardProduct(Category category, String name, double price) {
        StandardProduct product = new StandardProduct();
        product.setName(name);
        product.setCategory(category);
        product.setPrice(price);
        return (StandardProduct) productDAO.create(product);
    }

    // "admin wants to add a new custom product to an existing category"
    public CustomProduct createCustomProduct(Category category, String name, double laborCost, double hiddenMargin) {
        CustomProduct product = new CustomProduct();
        product.setName(name);
        product.setCategory(category);
        product.setLaborCost(laborCost);
        product.setHiddenMargin(hiddenMargin);
        return (CustomProduct) productDAO.create(product);
    }

    // "admin wants to delete a product that is no longer sold"
    public void deleteProduct(Long productId) {
        productDAO.delete(productId);
    }

    // ===== VARIANT =====

    // "customer wants to choose a variant of the selected product"
    public List<Variant> getVariantsByProduct(Long productId) {
        return variantDAO.getByProductId(productId);
    }

    // "admin wants to add a new variant (e.g. size/color) to a product"
    public Variant createVariant(Product product, String name, double price, int stock) {
        Variant variant = new Variant();
        variant.setProduct(product);
        variant.setName(name);
        variant.setPrice(price);
        variant.setStock(stock);
        variant.setOnSale(false);
        return variantDAO.create(variant);
    }

    // "admin wants to change the price of a variant"
    public void updatePrice(Long variantId, double newPrice) {
        Variant variant = getVariantOrThrow(variantId);
        variant.setPrice(newPrice);
        variantDAO.update(variant);
    }

    // "admin wants to update the stock count of a variant"
    public void updateStock(Long variantId, int newStock) {
        Variant variant = getVariantOrThrow(variantId);
        variant.setStock(newStock);
        variantDAO.update(variant);
    }

    // "admin wants to delete a variant that is no longer sold"
    public void deleteVariant(Long variantId) {
        variantDAO.delete(variantId);
    }

    // "admin wants to put a variant on sale"
    public void setOnSale(Long variantId, boolean onSale) {
        Variant variant = getVariantOrThrow(variantId);
        variant.setOnSale(onSale);
        variantDAO.update(variant);
    }

    // "customer wants to see items on sale"
    public List<Variant> getVariantsOnSale() {
        return variantDAO.getOnSale();
    }

    private Variant getVariantOrThrow(Long variantId) {
        Variant variant = variantDAO.getById(variantId);
        if (variant == null) {
            throw new ApiException(404, "Variant not found with id: " + variantId);
        }
        return variant;
    }
}