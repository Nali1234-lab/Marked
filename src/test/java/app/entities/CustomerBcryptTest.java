package app.entities;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

class CustomerBCryptTest {


    @Test
    void passwordIsHashed() {
        Customer customer = new Customer("Test", "test@bcrypt.dk", "hemmelig123", Role.CUSTOMER);
        String stored = customer.getPassword();

        assertNotEquals("hemmelig123", stored, "Passwordet må ikke gemmes i klartekst");
        assertTrue(stored.startsWith("$2a$"), "Passwordet skal være en BCrypt-hash");
    }


    @Test
    void passwordMatchesHash() {
        String correctPassword = "hemmelig123";
        String wrongPassword = "forkert";
        Customer customer = new Customer("Test", "test@bcrypt.dk", correctPassword, Role.CUSTOMER);

        assertTrue(BCrypt.checkpw(correctPassword, customer.getPassword()), "Det korrekte password skal matche");
        assertFalse(BCrypt.checkpw(wrongPassword, customer.getPassword()), "Et forkert password må ikke matche");
    }


    @Test
    void saltMakesHashesUnique() {

        String password1 = "hemmelig123";
        String password2 = "hemmelig123";


        String hash1 = BCrypt.hashpw(password1, BCrypt.gensalt());
        String hash2 = BCrypt.hashpw(password2, BCrypt.gensalt());

        assertNotEquals(hash1, hash2, "To hashes af samme password må ikke være ens pga. saltet");

        assertTrue(BCrypt.checkpw(password1, hash1), "Den første hash skal matche passwordet");
        assertTrue(BCrypt.checkpw(password2, hash2), "Den anden hash skal matche passwordet");
    }

    @Test
    void verifyPasswordMethod() {
        String correctPassword = "hemmelig123";
        String wrongPassword = "forkert";

        Customer customer = new Customer("Test", "test@bcrypt.dk", correctPassword, Role.CUSTOMER);

        assertTrue(customer.verifyPassword(correctPassword), "Metoden skal returnere true for korrekt password");
        assertFalse(customer.verifyPassword(wrongPassword), "Metoden skal returnere false for forkert password");
    }
}