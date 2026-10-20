package app.dtos;
import java.util.List;

public record DesignPreviewResponse(
        Long productId,
        String productName,
        List<String> selectedOptions,
        double finalPrice
) {

}