package app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "custom_products")
public class CustomProduct extends Product {

    private double laborCost;
    private double hiddenMargin;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "custom_product_materials",   // ← samme tabel-navn som før, så data bevares
            joinColumns = @JoinColumn(name = "custom_product_id"),
            inverseJoinColumns = @JoinColumn(name = "variant_id")
    )
    private List<Variant> designOptions = new ArrayList<>();

    @Override
    public double calculatePrice() {
        // Basispris UDEN kundens valg.
        // De enkelte valgmuligheders pris lægges til i ProductConfiguration.
        return laborCost + hiddenMargin;
    }

    public List<Variant> getOptionsBySlot(DesignSlot slot) {
        return designOptions.stream()
                .filter(v -> v.getDesignSlot() == slot)
                .toList();
    }
}