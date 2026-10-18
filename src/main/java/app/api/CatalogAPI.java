package app.api;

import app.dao.ProductConfigurationDAO;
import app.dao.ProductDAO;
import app.dto.*;
import app.entities.*;
import app.exceptions.ApiException;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

public class CatalogAPI {

    private final ProductDAO productDAO = new ProductDAO();
    private final ProductConfigurationDAO configDAO = new ProductConfigurationDAO();

    public void getAll(Context ctx) {
        List<Product> products = productDAO.getAll();
        List<ProductDTO> dtos = products.stream()
                .map(ProductDTO::from)
                .toList();
        ctx.json(dtos);
    }

    public void getById(Context ctx) {
        long id = ctx.pathParamAsClass("id", Long.class).get();
        Product product = productDAO.getById(id);
        if (product == null) {
            throw new ApiException(404, "Produkt ikke fundet med id: " + id);
        }
        ctx.json(ProductDetailsDTO.from(product));
    }

    public void preview(Context ctx) {
        long productId = ctx.pathParamAsClass("id", Long.class).get();
        DesignPreviewRequest req = ctx.bodyAsClass(DesignPreviewRequest.class);

        CustomProduct cp = getCustomProduct(productId);
        Variant color = findOption(cp, req.colorId(), DesignSlot.COLOR);
        Variant material = findOption(cp, req.materialId(), DesignSlot.MATERIAL);

        double finalPrice = cp.calculatePrice() + color.getPrice() + material.getPrice();

        List<String> selected = new ArrayList<>();
        selected.add(color.getName());
        selected.add(material.getName());

        ctx.json(new DesignPreviewResponse(cp.getId(), cp.getName(), selected, finalPrice));
    }

    public void configure(Context ctx) {
        Long customerId = ctx.attribute("customerId");
        if (customerId == null) {
            throw new ApiException(401, "Du skal være logget ind for at gemme et design");
        }

        long productId = ctx.pathParamAsClass("id", Long.class).get();
        SaveConfigurationRequest req = ctx.bodyAsClass(SaveConfigurationRequest.class);

        if (req.quantity() <= 0) {
            throw new ApiException(400, "Quantity skal være mindst 1");
        }

        CustomProduct cp = getCustomProduct(productId);
        Variant color = findOption(cp, req.colorId(), DesignSlot.COLOR);
        Variant material = findOption(cp, req.materialId(), DesignSlot.MATERIAL);

        double finalPrice = cp.calculatePrice() + color.getPrice() + material.getPrice();

        ProductConfiguration config = new ProductConfiguration();
        config.setProduct(cp);
        config.setSelectedOptions(List.of(color, material));
        config.setFinalPrice(finalPrice);
        config.setQuantity(req.quantity());

        ProductConfiguration saved = configDAO.create(config);

        List<String> selected = new ArrayList<>();
        selected.add(color.getName());
        selected.add(material.getName());

        ctx.status(201).json(new SavedConfigurationResponse(
                saved.getId(),
                cp.getName(),
                selected,
                finalPrice,
                req.quantity()
        ));
    }

    // Hjælpemetoder
    private CustomProduct getCustomProduct(long productId) {
        Product product = productDAO.getById(productId);
        if (product == null) {
            throw new ApiException(404, "Produkt ikke fundet med id: " + productId);
        }
        if (!(product instanceof CustomProduct cp)) {
            throw new ApiException(400, "Produktet er ikke et custom-produkt: " + productId);
        }
        return cp;
    }

    private Variant findOption(CustomProduct cp, Long variantId, DesignSlot expectedSlot) {
        if (variantId == null) {
            throw new ApiException(400, "Variant id er påkrævet");
        }
        return cp.getDesignOptions().stream()
                .filter(v -> v.getId().equals(variantId) && v.getDesignSlot() == expectedSlot)
                .findFirst()
                .orElseThrow(() -> new ApiException(400,
                        "Variant med id " + variantId + " er ikke en gyldig " + expectedSlot + " for dette produkt"));
    }
}