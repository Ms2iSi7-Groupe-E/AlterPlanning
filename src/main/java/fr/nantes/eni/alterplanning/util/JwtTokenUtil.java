package fr.nantes.eni.alterplanning.util;

import fr.nantes.eni.alterplanning.model.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ughostephan on 24/06/2017.
 */
@Component
public class JwtTokenUtil {

    private static final String CLAIM_KEY_SUBJECT = "sub";
    private static final String CLAIM_KEY_NAME = "name";
    private static final String CLAIM_KEY_EMAIL = "email";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Validate token boolean.
     *
     * @param token the token
     * @param user  the user
     * @return the boolean
     */
    public Boolean validateToken(String token, UserEntity user) {
        final Integer id = getIdFromToken(token);
        return (id.equals(user.getId()) && !isTokenExpired(token));
    }

    /**
     * Generate token string.
     *
     * @param user the user
     * @return the string
     */
    public String generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_SUBJECT, String.valueOf(user.getId()));
        claims.put(CLAIM_KEY_NAME, user.getName());
        claims.put(CLAIM_KEY_EMAIL, user.getEmail());
        return generateToken(claims);
    }

    public Integer getIdFromToken(String token) {
        Integer id;
        try {
            final Claims claims = getClaimsFromToken(token);
            id = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            id = null;
        }
        return id;
    }

    private Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
