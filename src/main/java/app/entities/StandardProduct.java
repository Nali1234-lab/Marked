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

public class StandardProduct extends Product {

    private double price;

    @Override
    public double calculatePrice() {
        return price; }
}