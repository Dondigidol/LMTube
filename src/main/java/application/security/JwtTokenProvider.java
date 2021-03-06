package application.security;

import application.entities.Role;
import application.entities.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static application.security.SecurityConstants.EXPIRATION_TIME;
import static application.security.SecurityConstants.SECRET;

@Component
public class JwtTokenProvider {

    public String generateToken(Authentication authentication){
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("fullName", user.getFullName());
        claims.put("position", user.getPosition());
        claims.put("role", user.getRole());

        return Jwts.builder()
                .setSubject(user.getUsername())
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException e){
            System.out.println("Invalid JWT Signature");
        } catch (MalformedJwtException e){
            System.out.println("Invalid JWT Token");
        } catch (ExpiredJwtException e){
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException e){
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException e){
            System.out.println("JWT claims string is empty");
        }
        return false;
    }

    public String getUserNameFromJWT(String token){
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        return (String) claims.get("username");
    }

    public User getUserFromJWT(String token){
        Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
        User user = new User();
        user.setUsername((String) claims.get("username"));
        user.setFullName((String) claims.get("fullName"));
        user.setPosition((String) claims.get("position"));
        user.setRole(Role.valueOf(((String)claims.get("role")).toUpperCase()));
        return user;
    }


}
