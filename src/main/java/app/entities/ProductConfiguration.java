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
@Table(name = "product_configurations")
public class ProductConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "custom_product_id", nullable = false)
    private CustomProduct product;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "configuration_selections",
            joinColumns = @JoinColumn(name = "configuration_id"),
            inverseJoinColumns = @JoinColumn(name = "variant_id")
    )
    private List<Variant> selectedOptions = new ArrayList<>();

    @Column(nullable = false)
    private double finalPrice;

    @Column(nullable = false)
    private int quantity;
}