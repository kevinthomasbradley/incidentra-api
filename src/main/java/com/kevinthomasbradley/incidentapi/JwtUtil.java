package com.kevinthomasbradley.incidentapi;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;

/**
 * Utility class for handling JSON Web Token (JWT) operations in the application.
 * <p>
 * This class provides methods for generating, parsing, and validating JWT tokens.
 * It uses the HS256 algorithm and a secret key defined in the application properties.
 * </p>
 * <ul>
 *   <li>Generates JWT tokens for authenticated users with a configurable expiration time.</li>
 *   <li>Extracts the username (subject) and expiration date from a JWT.</li>
 *   <li>Validates tokens for authenticity and expiration.</li>
 * </ul>
 * <b>Configuration:</b>
 * <ul>
 *   <li><code>jwt.secret</code>: Secret key for signing tokens (must be at least 256 bits for HS256).</li>
 *   <li><code>jwt.expiration</code>: Token validity period in milliseconds.</li>
 * </ul>
 */
@Component
public class JwtUtil {
    
    /**
     * Secret key for signing and validating JWT tokens.
     * Injected from the application properties (jwt.secret).
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Token expiration time in milliseconds.
     * Injected from the application properties (jwt.expiration).
     */
    @Value("${jwt.expiration}")
    private long expiration;
    
    /**
     * Generates a JWT token for the given user details.
     *
     * @param userDetails the user details for which to generate the token
     * @return a signed JWT token as a String
     */
    public String generateToken(UserDetails userDetails) {
        Key key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token the JWT token
     * @return the username (subject) embedded in the token
     */
    public String extractUsername(String token) {
        Key key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    /**
     * Validates the JWT token for the given user details.
     * Checks that the username matches and the token is not expired.
     *
     * @param token the JWT token to validate
     * @param userDetails the user details to match against the token's subject
     * @return true if the token is valid and not expired, false otherwise
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Checks if the JWT token is expired.
     *
     * @param token the JWT token
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date of the token
     */
    private Date extractExpiration(String token) {
        Key key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration();
    }
}
