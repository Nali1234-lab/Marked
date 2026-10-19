package app.utils;

import app.entities.Customer;
import app.exceptions.ApiException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class JwtUtil {

    private static final String SECRET = "marked-super-secret-jwt-key-min-256-bits-long-ok-for-hmac";
    private static final String ISSUER = "marked-api";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SECRET);

    public static String generateToken(Customer customer) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withSubject(customer.getId().toString())
                .withClaim("role", customer.getRole().name())
                .withClaim("email", customer.getEmail())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(8, ChronoUnit.HOURS))
                .sign(ALGORITHM);
    }

    public static DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(ALGORITHM)
                    .withIssuer(ISSUER)
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new ApiException(401, "Ugyldigt eller udløbet token");
        }
    }

    public static Long getCustomerId(String token) {
        return Long.parseLong(verifyToken(token).getSubject());
    }

    public static String getRole(String token) {
        return verifyToken(token).getClaim("role").asString();
    }
}