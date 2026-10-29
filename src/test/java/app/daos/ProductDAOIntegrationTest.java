package app.daos;

import app.entities.Category;
import app.entities.Product;
import app.entities.StandardProduct;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductDAOIntegrationTest extends BaseTest {

    private static ProductDAO productDAO;
    private static CategoryDAO categoryDAO;

    @BeforeAll
    static void initDAO() {
        productDAO = new ProductDAO(emf);
        categoryDAO = new CategoryDAO(emf);
    }

    @Test
    void create_shouldPersistStandardProduct() {
        Category category = new Category();
        category.setName("Test Kategori");
        categoryDAO.create(category);

        StandardProduct product = new StandardProduct();
        product.setName("Test Produkt");
        product.setPrice(100.0);
        product.setCategory(category);

        Product saved = productDAO.create(product);

        assertNotNull(saved.getId());
        assertEquals("Test Produkt", saved.getName());
        assertEquals(100.0, saved.calculatePrice());
    }

    @Test
    void getByCategory_shouldReturnProducts_whenCategoryHasProducts() {
        Category category = new Category();
        category.setName("Test Kategori 2");
        categoryDAO.create(category);

        StandardProduct product = new StandardProduct();
        product.setName("Produkt i kategori");
        product.setPrice(50.0);
        product.setCategory(category);
        productDAO.create(product);

        List<Product> found = productDAO.getByCategory(category.getId());

        assertEquals(1, found.size());
        assertEquals("Produkt i kategori", found.get(0).getName());
    }
}