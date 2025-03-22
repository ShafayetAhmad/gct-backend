package com.example.auth.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.Base64;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);
    private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        try {
            // Ensure the secret key is properly formatted
            String formattedKey = secretKey.replaceAll("\\s+", "");
            byte[] keyBytes = Decoders.BASE64.decode(formattedKey);
            this.key = Keys.hmacShaKeyFor(keyBytes);
            logger.info("JWT secret key initialized successfully with length: {}", keyBytes.length);
            logger.info("Secret key (first 10 chars): {}",
                    formattedKey.substring(0, Math.min(10, formattedKey.length())));
            logger.info("Secret key (base64): {}", formattedKey);
        } catch (Exception e) {
            logger.error("Failed to initialize JWT secret key: {}", e.getMessage());
            throw new IllegalStateException("Could not initialize JWT secret key", e);
        }
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        try {
            String token = Jwts.builder()
                    .setClaims(extraClaims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(key, ALGORITHM)
                    .compact();
            logger.info("Token generated successfully for user: {}", userDetails.getUsername());
            logger.info("Generated token: {}", token);

            // Log token parts for debugging
            String[] parts = token.split("\\.");
            logger.info("Token header: {}", new String(Base64.getDecoder().decode(parts[0])));
            logger.info("Token payload: {}", new String(Base64.getDecoder().decode(parts[1])));
            logger.info("Token signature: {}", parts[2]);

            return token;
        } catch (Exception e) {
            logger.error("Error generating token: {}", e.getMessage());
            throw new JwtException("Failed to generate token", e);
        }
    }

    public String extractUsername(String token) {
        try {
            String username = extractClaim(token, Claims::getSubject);
            logger.debug("Extracted username from token: {}", username);
            return username;
        } catch (Exception e) {
            logger.error("Error extracting username from token: {}", e.getMessage());
            throw e;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean isExpired = isTokenExpired(token);
            boolean isUsernameMatch = username.equals(userDetails.getUsername());

            logger.info("Token validation for user {}: usernameMatch={}, isExpired={}",
                    username, isUsernameMatch, isExpired);

            return isUsernameMatch && !isExpired;
        } catch (JwtException e) {
            logger.error("Token validation error: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            boolean expired = expiration.before(new Date());
            logger.debug("Token expiration check: expiration={}, current={}, isExpired={}",
                    expiration, new Date(), expired);
            return expired;
        } catch (Exception e) {
            logger.error("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            logger.debug("Attempting to parse token: {}", token);

            // Split the token into its parts
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new JwtException("Invalid token format");
            }

            // Log the extracted signature
            String extractedSignature = parts[2];
            logger.info("Extracted signature from token: {}", extractedSignature);

            // Log token parts for debugging
            logger.info("Token header: {}", new String(Base64.getDecoder().decode(parts[0])));
            logger.info("Token payload: {}", new String(Base64.getDecoder().decode(parts[1])));
            logger.info("Token signature: {}", parts[2]);

            // Parse and validate the token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            logger.debug("Successfully parsed token claims: {}", claims);
            return claims;
        } catch (Exception e) {
            logger.error("Error parsing JWT token: {}", e.getMessage());
            logger.error("Token that caused error: {}", token);
            throw new JwtException("Failed to parse token", e);
        }
    }
}