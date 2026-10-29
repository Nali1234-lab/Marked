package app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "customers")
public class Customer implements ISecurityUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Constructor der hasher passwordet med det samme!
    public Customer(String name, String email, String plainPassword, Role role) {
        this.name = name;
        this.email = email;
        this.password = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        this.role = role;
    }


    @Override
    public Set<String> getRolesAsStrings() {
        if (role == null) return Set.of();
        return Set.of(role.name());
    }

    @Override
    public boolean verifyPassword(String pw) {
        try {
            return BCrypt.checkpw(pw, this.password);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void addRole(Role role) {
        this.role = role;
    }

    @Override
    public void removeRole(String role) {
        if (this.role != null && this.role.name().equals(role)) {
            this.role = null;
        }
    }
}