package xyz.spoonmap.server.authentication.filter;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xyz.spoonmap.server.authentication.JwtGenerator;
import xyz.spoonmap.server.authentication.dto.LoginRequest;
import xyz.spoonmap.server.exception.member.UnauthorizedException;

@Slf4j
public class AuthenticateFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtGenerator jwtGenerator;

    public AuthenticateFilter(AuthenticationManager authenticationManager, JwtGenerator jwtGenerator) {
        super(authenticationManager);
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

            return getAuthenticationManager().authenticate(token);

        } catch (IOException e) {
            log.warn(e.getMessage());
            throw new UnauthorizedException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String token = jwtGenerator.createJwt(authResult.getName());
        Cookie jwtCookie = new Cookie(JwtGenerator.JWT_COOKIE, token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");

        log.info("token = {}", token);
        response.addCookie(jwtCookie);
        response.setStatus(OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.sendError(UNAUTHORIZED.value(), failed.getMessage());
    }

}
