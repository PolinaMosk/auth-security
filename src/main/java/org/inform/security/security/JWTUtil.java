package org.inform.security.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.inform.security.exceptions.InvalidJwtAuthenticationException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.function.Function;

@Component
public class JWTUtil {
    private static final Long validityMs = 43200000L;
    private static final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    public static String extractLogin(String token){
        return extractClaim(token, Claims::getSubject);
    }
    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private static Claims extractAllClaims(String token){
        SecretKey secretKey = new SecretKeySpec(key.getEncoded(), key.getAlgorithm());
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
    }
    public static String createToken(String login) {
        Claims claims = Jwts.claims().setSubject(login);

        Date now = new Date();
        Date validity = new Date(System.currentTimeMillis() + validityMs);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    public static boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException ex) {
            throw new InvalidJwtAuthenticationException("Expired or invalid token");
        }
    }
}
