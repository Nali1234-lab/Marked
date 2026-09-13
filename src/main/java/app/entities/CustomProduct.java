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
    // @ManyToMany: en variant (fx et bestemt stof) kan bruges i mange custom-produkter,
    // og ét custom-produkt kan bruge mange varianter (fx både stof OG farve)
    @JoinTable(
            name = "custom_product_materials",
            // navn på den "usynlige" mellemtabel, Hibernate selv opretter,
            // fordi mange-til-mange ikke kan ligge direkte i én af de to tabeller
            joinColumns = @JoinColumn(name = "custom_product_id"),
            inverseJoinColumns = @JoinColumn(name = "variant_id")
    )
    private List<Variant> materials = new ArrayList<>();

    @Override
    public double calculatePrice() {
        double materialCost = materials.stream()
                .mapToDouble(Variant::getPrice)
                .sum();
        return materialCost + laborCost + hiddenMargin;
    }
}