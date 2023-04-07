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
import xyz.spoonmap.server.exception.domain.member.UnauthorizedException;

@Slf4j
@Component
public class JwtGenerator {

    // 유효시간: 1시간
    private static final long EXPIRATION = 1000L * 60 * 60;
    private static final long REFRESH_EXPIRATION = 1000L * 60 * 60 * 6;
    public static final String JWT_COOKIE = "FOR_LOGIN";
    public static final String REFRESH_COOKIE = "FOR_REFRESH";

    private final UserDetailsService userDetailsService;
    private final Key key;

    public JwtGenerator(UserDetailsService userDetailsService, @Value("${jwt.secret}") String secret) {
        this.userDetailsService = userDetailsService;
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createJwt(final String email) {
        return createToken(email, EXPIRATION);
    }

    public String createRefreshToken(final String email) {
        return createToken(email, REFRESH_EXPIRATION);
    }

    private String createToken(final String email, final long expiration) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", "USER");
        Date issuedAt = new Date();

        return Jwts.builder()
                   .setClaims(claims)
                   .setIssuedAt(issuedAt)
                   .setExpiration(new Date(issuedAt.getTime() + expiration))
                   .signWith(key, SignatureAlgorithm.HS256)
                   .compact();
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
        Cookie[] cookies = request.getCookies();

        if (Objects.isNull(cookies)) {
            return null;
        }

        Cookie jwt = Arrays.stream(cookies)
                           .filter(cookie -> Objects.equals(cookie.getName(), JWT_COOKIE))
                           .findFirst()
                           .orElseThrow(UnauthorizedException::new);

        return jwt.getValue();
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
