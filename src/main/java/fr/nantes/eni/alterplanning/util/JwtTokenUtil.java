package fr.nantes.eni.alterplanning.util;

import fr.nantes.eni.alterplanning.bean.User;
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
    public Boolean validateToken(String token, User user) {
        final String uid = getUidFromToken(token);
        return (uid.equals(user.getUid()) && !isTokenExpired(token));
    }

    /**
     * Generate token string.
     *
     * @param user the user
     * @return the string
     */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_SUBJECT, user.getUid());
        claims.put(CLAIM_KEY_NAME, user.getFirstname() + " " + user.getLastname());
        claims.put(CLAIM_KEY_EMAIL, user.getEmail());
        return generateToken(claims);
    }

    public String getUidFromToken(String token) {
        String uid;
        try {
            final Claims claims = getClaimsFromToken(token);
            uid = claims.getSubject();
        } catch (Exception e) {
            uid = null;
        }
        return uid;
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
