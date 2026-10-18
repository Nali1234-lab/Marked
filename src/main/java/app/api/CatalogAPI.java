package app.api;

import app.dao.ProductDAO;
import app.dto.DesignPreviewRequest;
import app.dto.DesignPreviewResponse;
import app.dto.ProductDTO;
import app.dto.ProductDetailsDTO;
import app.entities.CustomProduct;
import app.entities.DesignSlot;
import app.entities.Product;
import app.entities.Variant;
import app.exceptions.ApiException;
import io.javalin.apibuilder.EndpointGroup;

import java.util.ArrayList;
import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

public class CatalogAPI {

    private final ProductDAO productDAO = new ProductDAO();

    public EndpointGroup getRoutes() {
        return () -> {

            // GET /api/products
            get("/api/products", ctx -> {
                List<Product> products = productDAO.getAll();
                List<ProductDTO> dtos = products.stream()
                        .map(ProductDTO::from)
                        .toList();
                ctx.json(dtos);
            });

            // GET /api/products/{id}
            get("/api/products/{id}", ctx -> {
                long id = ctx.pathParamAsClass("id", Long.class).get();
                Product product = productDAO.getById(id);

                if (product == null) {
                    throw new ApiException(404, "Produkt ikke fundet med id: " + id);
                }

                ctx.json(ProductDetailsDTO.from(product));
            });

            // POST /api/custom-products/{id}/preview
            post("/api/custom-products/{id}/preview", ctx -> {
                long productId = ctx.pathParamAsClass("id", Long.class).get();
                DesignPreviewRequest req = ctx.bodyAsClass(DesignPreviewRequest.class);

                if (req.colorId() == null) {
                    throw new ApiException(400, "Field 'colorId' er påkrævet");
                }
                if (req.materialId() == null) {
                    throw new ApiException(400, "Field 'materialId' er påkrævet");
                }

                Product product = productDAO.getById(productId);
                if (product == null) {
                    throw new ApiException(404, "Produkt ikke fundet med id: " + productId);
                }
                if (!(product instanceof CustomProduct cp)) {
                    throw new ApiException(400, "Produktet er ikke et custom-produkt: " + productId);
                }

                // Find de valgte varianter i produktets designOptions
                Variant color = findOptionById(cp, req.colorId(), DesignSlot.COLOR);
                Variant material = findOptionById(cp, req.materialId(), DesignSlot.MATERIAL);

                // Beregn pris: basispris (laborCost + hiddenMargin) + farve + materiale
                double finalPrice = cp.calculatePrice() + color.getPrice() + material.getPrice();

                List<String> selected = new ArrayList<>();
                selected.add(color.getName());
                selected.add(material.getName());

                ctx.json(new DesignPreviewResponse(
                        cp.getId(),
                        cp.getName(),
                        selected,
                        finalPrice
                ));
            });

        };
    }

    private Variant findOptionById(CustomProduct cp, Long variantId, DesignSlot expectedSlot) {
        return cp.getDesignOptions().stream()
                .filter(v -> v.getId().equals(variantId) && v.getDesignSlot() == expectedSlot)
                .findFirst()
                .orElseThrow(() -> new ApiException(400,
                        "Variant med id " + variantId + " er ikke en gyldig " + expectedSlot + " for dette produkt"));
    }
}