package dev.jagan.user_management.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret; // The secret key used for signing the JWT

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs; // JWT expiration time in milliseconds

    // Method to generate a JWT token from the authentication information
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal(); // Get the user details from the authentication object

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs); // Calculate the expiration date based on the configured expiration time
        // Build and return the JWT token
        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId())) // Set the user ID as the subject of the token
                .setIssuedAt(new Date()) // Set the issue date of the token
                .setExpiration(expiryDate) // Set the expiration date of the token
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // Sign the token using HS512 algorithm and the secret key
                .compact(); // Compact the JWT into a string
    }

    // Method to extract the user ID from the JWT token
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()    // Parse the JWT token
                .setSigningKey(jwtSecret) // Use the secret key to verify the signature
                .parseClaimsJws(token)  // Parse the claims (the body of the token)
                .getBody(); // Get the body of the token (the claims)
        // Return the user ID, which is stored in the subject field of the token
        return Long.parseLong(claims.getSubject());
    }
    // Method to validate the JWT token
    public boolean validateToken(String authToken) {
        try {
            // Try to parse and validate the token using the secret key
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true; // If no exceptions are thrown, the token is valid
        } catch (SignatureException ex) {
            // Invalid JWT signature
        } catch (MalformedJwtException ex) {
            // Invalid JWT token
        } catch (ExpiredJwtException ex) {
            // Expired JWT token
        } catch (UnsupportedJwtException ex) {
            // Unsupported JWT token
        } catch (IllegalArgumentException ex) {
            // JWT claims string is empty
        }
        return false; // If any exception occurs, the token is invalid
    }

}
