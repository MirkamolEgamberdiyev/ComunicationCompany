package pdp.uz.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import pdp.uz.entity.Role;

import java.util.Date;
import java.util.Set;

@Component
public class JwtProvider {

    private static final long EXPIRE_TIME = 1000 * 60 * 60L;

    private static final String secretKey = "secretKey";

    public String generateToken(String username, Set<Role> roles) {
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        return Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            return Jwts
                    .parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody().getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}
