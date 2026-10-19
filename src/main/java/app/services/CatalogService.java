package app.services;

import app.dao.CategoryDAO;
import app.dao.ProductDAO;
import app.dao.VariantDAO;
import app.dto.CreateProductRequest;
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
    public List<Category> getAllCategories() { return categoryDAO.getAll(); }

    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryDAO.create(category);
    }

    public void deleteCategory(Long categoryId) {
        List<Product> products = productDAO.getByCategory(categoryId);
        if (!products.isEmpty()) {
            throw new ApiException(400, "Cannot delete a category that still has products");
        }
        categoryDAO.delete(categoryId);
    }

    // ===== PRODUCT =====
    public List<Product> getProductsByCategory(Long categoryId) { return productDAO.getByCategory(categoryId); }

    public List<Product> getAllProducts() { return productDAO.getAll(); }

    public Product createProduct(CreateProductRequest req) {
        Category category = categoryDAO.getById(req.categoryId());
        if (category == null) throw new ApiException(404, "Kategori ikke fundet med id: " + req.categoryId());

        Product product;
        if ("STANDARD".equals(req.type())) {
            StandardProduct sp = new StandardProduct();
            sp.setName(req.name());
            sp.setPrice(req.price());
            sp.setCategory(category);
            product = sp;
        } else if ("CUSTOM".equals(req.type())) {
            CustomProduct cp = new CustomProduct();
            cp.setName(req.name());
            cp.setLaborCost(req.laborCost());
            cp.setHiddenMargin(req.hiddenMargin());
            cp.setCategory(category);
            product = cp;
        } else {
            throw new ApiException(400, "Ugyldig produkttype: " + req.type());
        }
        return productDAO.create(product);
    }

    public void deleteProduct(Long productId) { productDAO.delete(productId); }

    // ===== VARIANT =====
    public List<Variant> getVariantsByProduct(Long productId) { return variantDAO.getByProductId(productId); }

    // NY: Opret variant med productId og designSlot
    public Variant createVariant(Long productId, String name, double price, int stock, DesignSlot designSlot) {
        Product product = productDAO.getById(productId);
        if (product == null) {
            throw new ApiException(404, "Produkt ikke fundet med id: " + productId);
        }

        Variant variant = new Variant();
        variant.setProduct(product);
        variant.setName(name);
        variant.setPrice(price);
        variant.setStock(stock);
        variant.setDesignSlot(designSlot);
        variant.setOnSale(false);

        return variantDAO.create(variant);
    }

    public void updatePrice(Long variantId, double newPrice) {
        Variant variant = getVariantOrThrow(variantId);
        variant.setPrice(newPrice);
        variantDAO.update(variant);
    }

    public void updateStock(Long variantId, int newStock) {
        Variant variant = getVariantOrThrow(variantId);
        variant.setStock(newStock);
        variantDAO.update(variant);
    }

    public void deleteVariant(Long variantId) { variantDAO.delete(variantId); }

    public void setOnSale(Long variantId, boolean onSale) {
        Variant variant = getVariantOrThrow(variantId);
        variant.setOnSale(onSale);
        variantDAO.update(variant);
    }

    public List<Variant> getVariantsOnSale() { return variantDAO.getOnSale(); }

    private Variant getVariantOrThrow(Long variantId) {
        Variant variant = variantDAO.getById(variantId);
        if (variant == null) throw new ApiException(404, "Variant not found with id: " + variantId);
        return variant;
    }
}