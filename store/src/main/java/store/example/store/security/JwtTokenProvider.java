package store.example.store.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Service pour la gestion des tokens JWT
 * 
 * Cette classe gère la création, validation et extraction d'informations
 * des tokens JWT utilisés pour l'authentification.
 */
@Component
public class JwtTokenProvider {

    private final SecretKey jwtSecret;
    private final int jwtExpirationInMs;

    /**
     * Constructeur qui initialise la clé secrète et la durée d'expiration
     * 
     * @param jwtSecret         Clé secrète pour signer les tokens
     * @param jwtExpirationInMs Durée d'expiration en millisecondes
     */
    public JwtTokenProvider(@Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expiration}") int jwtExpirationInMs) {
        this.jwtSecret = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    /**
     * Génère un token JWT pour un utilisateur authentifié
     * 
     * @param authentication Objet d'authentification Spring Security
     * @return Token JWT sous forme de chaîne
     */
    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Instant now = Instant.now();
        Instant expiryDate = now.plus(jwtExpirationInMs, ChronoUnit.MILLIS);

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiryDate))
                .signWith(jwtSecret, Jwts.SIG.HS512)
                .compact();
    }

    /**
     * Génère un token JWT pour un nom d'utilisateur spécifique
     * 
     * @param username Nom d'utilisateur (email)
     * @return Token JWT sous forme de chaîne
     */
    public String generateTokenFromUsername(String username) {
        Instant now = Instant.now();
        Instant expiryDate = now.plus(jwtExpirationInMs, ChronoUnit.MILLIS);

        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiryDate))
                .signWith(jwtSecret, Jwts.SIG.HS512)
                .compact();
    }

    /**
     * Extrait le nom d'utilisateur du token JWT
     * 
     * @param token Token JWT
     * @return Nom d'utilisateur (email)
     */
    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    /**
     * Valide un token JWT
     * 
     * @param authToken Token JWT à valider
     * @return true si le token est valide, false sinon
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(jwtSecret)
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (SecurityException ex) {
            System.err.println("Signature JWT invalide");
        } catch (MalformedJwtException ex) {
            System.err.println("Token JWT malformé");
        } catch (ExpiredJwtException ex) {
            System.err.println("Token JWT expiré");
        } catch (UnsupportedJwtException ex) {
            System.err.println("Token JWT non supporté");
        } catch (IllegalArgumentException ex) {
            System.err.println("JWT claims string est vide");
        }
        return false;
    }

    /**
     * Extrait la date d'expiration du token
     * 
     * @param token Token JWT
     * @return Date d'expiration
     */
    public Date getExpirationDateFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(jwtSecret)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getExpiration();
    }

    /**
     * Vérifie si le token est expiré
     * 
     * @param token Token JWT
     * @return true si le token est expiré
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromJWT(token);
        return expiration.before(new Date());
    }
}