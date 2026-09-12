package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
@Inheritance(strategy = InheritanceType.JOINED)
// JOINED: fælles felter (id, name, category) gemmes i "products"-tabellen,
// mens hver underklasses egne felter gemmes i deres EGEN tabel, koblet via samme id
public abstract class Product {
    // "abstract" betyder: man kan ALDRIG skrive "new Product()" direkte,
    // kun de konkrete underklasser (StandardProduct, CustomProduct)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public abstract double calculatePrice();
    // "abstract" metode: en KONTRAKT der siger "enhver underklasse
    // SKAL selv skrive sin egen udgave af denne metode"
}