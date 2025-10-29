package ia.code.gateway_service.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;


@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key getSignKey() { return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); }


    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException e) {
            System.out.println("Token Invalido: " + e.getMessage());
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public void debugToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            System.out.println("=== TOKEN DEBUG ===");
            System.out.println("Subject: " + claims.getSubject());
            System.out.println("ID: " + claims.get("id", Integer.class));  // ← Como Integer
            System.out.println("Role: " + claims.get("role", String.class)); // ← Como String
            System.out.println("All claims: " + claims);

            // Debug de tipos
            claims.forEach((key, value) -> {
                System.out.println("Claim [" + key + "]: " + value + " (type: " + (value != null ? value.getClass().getSimpleName() : "null") + ")");
            });
        } catch (Exception e) {
            System.out.println("Error debugging token: " + e.getMessage());
        }
    }
}
