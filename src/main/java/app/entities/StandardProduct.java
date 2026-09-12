package app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "standard_products")
// "extends Product": arver alle felter og adfærd fra Product (id, name, category)
public class StandardProduct extends Product {

    private double price; // den faste, synlige pris - unik for netop StandardProduct

    @Override // fortæller Java: "dette ERSTATTER Product's abstrakte version"
    public double calculatePrice() {
        return price; // simplest muligt: prisen ER bare selve prisen
    }
}