package xyz.spoonmap.server.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import xyz.spoonmap.server.exception.member.UnauthorizedException;

@Slf4j
@Component
public class JwtGenerator {

    // 유효시간: 1시간
    private static final long EXPIRATION = 1000L * 60 * 60;
    public static final String COOKIE_NAME = "JWT";
    public static final String BEARER = "BEARER";

    private final UserDetailsService userDetailsService;
    private final Key key;

    public JwtGenerator(UserDetailsService userDetailsService, @Value("${jwt.secret}") String secret) {
        this.userDetailsService = userDetailsService;
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(final String email) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", "ROLE_USER");
        Date issuedAt = new Date();

        String jwt = Jwts.builder()
                             .setClaims(claims)
                             .setIssuedAt(issuedAt)
                             .setExpiration(new Date(issuedAt.getTime() + EXPIRATION))
                             .signWith(key, SignatureAlgorithm.HS256)
                             .compact();

        return BEARER + " " + jwt;
    }

    public Authentication getAuthentication(final String token) {
        String email = this.getSubject(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getSubject(final String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(key)
                   .build()
                   .parseClaimsJws(token)
                   .getBody()
                   .getSubject();
    }

    public String getToken(HttpServletRequest request) {
        Cookie jwt = Arrays.stream(request.getCookies())
                           .filter(cookie -> Objects.equals(cookie.getName(), COOKIE_NAME))
                           .findFirst()
                           .orElseThrow(UnauthorizedException::new);

        return Objects.requireNonNull(jwt.getValue());
    }

    public boolean isValid(final String token) {
        try {
            return verifyToken(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.warn("Invalid JWT {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.warn("Expired JWT {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT Token {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.warn("JWT claim is invalid {}", e.getMessage());
        }

        return false;
    }

    private boolean verifyToken(final String token) {
        if (isNotBearerToken(token)) {
            return false;
        }

        return isValidYet(token);
    }

    private boolean isNotBearerToken(final String token) {
        return token.substring(0, BEARER.length()).equalsIgnoreCase(BEARER);
    }

    private boolean isValidYet(final String token) {
        String jwt = token.split(" ")[1].trim();
        Jws<Claims> claims = Jwts.parserBuilder()
                                 .setSigningKey(key)
                                 .build()
                                 .parseClaimsJws(jwt);

        return !claims.getBody()
                      .getExpiration()
                      .before(new Date());
    }

}
