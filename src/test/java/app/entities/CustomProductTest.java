package app.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomProductTest {

    @Test
    void calculatePrice_shouldAddLaborCostAndHiddenMargin() {
        // Arrange
        CustomProduct product = new CustomProduct();
        product.setLaborCost(50.0);
        product.setHiddenMargin(20.0);

        // Act
        double price = product.calculatePrice();

        // Assert
        assertEquals(70.0, price, 0.01);
    }

    @Test
    void calculatePrice_shouldWorkWithZeroValues() {
        CustomProduct product = new CustomProduct();
        product.setLaborCost(0.0);
        product.setHiddenMargin(0.0);

        double price = product.calculatePrice();

        assertEquals(0.0, price, 0.01);
    }
}