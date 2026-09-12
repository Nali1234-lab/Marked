package entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "custom_products")
public class CustomProduct extends Product {

    private double laborCost;    // skjult arbejdsløn - kunden ser den ALDRIG direkte
    private double hiddenMargin; // skjult avance - kunden ser den ALDRIG direkte

    @Override
    public double calculatePrice() {
        // Foreløbig simpel version - senere skal materialepriser lægges til her,
        // når vi bygger relationen til Materiale/Variant
        return laborCost + hiddenMargin;
    }
}