package entities;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="orders")

public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime orderDate;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String deliveryStreet;
    private String deliveryZipCode;
    private String deliveryCity;
    @ManyToOne(fetch = FetchType.LAZY)
    // @ManyToOne: mange Order-objekter kan pege på én Customer (relationstypen vi besluttede)
    // fetch = LAZY: hent kun kundedata, når det faktisk efterspørges i koden
    @JoinColumn(name = "customer_id", nullable = false)
    // opretter en kolonne "customer_id" i orders-tabellen (fremmednøglen)
    // nullable = false: hver ordre SKAL have en kunde, kan ikke være tom
    @OnDelete(action = OnDeleteAction.CASCADE)
    // hvis kunden denne ordre peger på bliver slettet, slet automatisk også denne ordre
    private Customer customer; // selve referencen til kundens objekt

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

}
