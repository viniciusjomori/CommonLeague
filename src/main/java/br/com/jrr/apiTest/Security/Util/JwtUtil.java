package br.com.jrr.apiTest.Security.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.issuer}")
    private String issuer;

    public String generateToken(UserDetails user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(
        Map<String, Object> extraClaims,
        UserDetails user
    ) {
        return buildToken(extraClaims, user, jwtExpiration);
    }

    public String buildToken(
            Map<String, Object> extraClaims,
            UserDetails user,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .setIssuer(issuer)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private String extractIssuer(String token) {
        return extractClaim(token, Claims::getIssuer);
    }

    public <R> R extractClaim(String token, Function<Claims, R> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails user) {
        if(!isTokenValid(token)) return false;
        String username = extractSubject(token);
        return (username.equals(user.getUsername()));
    }

    public boolean isTokenValid(String token) {
        if(token == null) return false;
        if(!token.startsWith("Bearer ")) return false;
        token = token.substring(7);
        String issuer = extractIssuer(token);
        boolean isExpired = extractExpiration(token).before(new Date());
        return !isExpired && issuer.equals(issuer);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}