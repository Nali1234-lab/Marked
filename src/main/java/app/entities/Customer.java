package app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // fortæller Hibernate at denne klasse skal blive til en tabel i databasen
@Getter // Lombok laver automatisk get-metoder for alle felter
@Setter // Lombok laver automatisk set-metoder for alle felter
@NoArgsConstructor // Lombok laver en tom constructor, som Hibernate kræver
@Table(name = "customers") // navngiver selve tabellen i databasen "customers"
public class Customer {

    @Id // markerer dette felt som tabellens primærnøgle
    @GeneratedValue(strategy = GenerationType.IDENTITY) // databasen genererer automatisk et unikt id
    private Long id;

    @Column(nullable = false)
    // opretter en kolonne, der ikke må være tom - navn er påkrævet
    private String name;

    @Column(nullable = false, unique = true)
    // nullable = false: email er påkrævet
    // unique = true: ingen to kunder må have samme email (bruges også til login)
    private String email;

    @Column(nullable = false)
    // password er påkrævet
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}