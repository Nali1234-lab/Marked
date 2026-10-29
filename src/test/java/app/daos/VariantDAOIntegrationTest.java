package app.daos;

import app.entities.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VariantDAOIntegrationTest extends BaseTest {

    private static VariantDAO variantDAO;
    private static ProductDAO productDAO;
    private static CategoryDAO categoryDAO;

    @BeforeAll
    static void initDAO() {
        variantDAO = new VariantDAO(emf);
        productDAO = new ProductDAO(emf);
        categoryDAO = new CategoryDAO(emf);
    }

    @Test
    void getOnSale_shouldReturnOnlyVariantsOnSale() {
        Category category = new Category();
        category.setName("Test Kategori");
        categoryDAO.create(category);

        StandardProduct product = new StandardProduct();
        product.setName("Produkt");
        product.setPrice(100.0);
        product.setCategory(category);
        productDAO.create(product);

        Variant v1 = new Variant();
        v1.setName("På tilbud");
        v1.setPrice(50.0);
        v1.setStock(10);
        v1.setOnSale(true);
        v1.setProduct(product);
        variantDAO.create(v1);

        Variant v2 = new Variant();
        v2.setName("Ikke på tilbud");
        v2.setPrice(100.0);
        v2.setStock(10);
        v2.setOnSale(false);
        v2.setProduct(product);
        variantDAO.create(v2);

        List<Variant> onSale = variantDAO.getOnSale();

        assertEquals(1, onSale.size());
        assertEquals("På tilbud", onSale.get(0).getName());
    }
}