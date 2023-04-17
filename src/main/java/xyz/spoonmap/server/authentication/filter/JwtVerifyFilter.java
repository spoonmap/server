package xyz.spoonmap.server.authentication.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.spoonmap.server.authentication.JwtGenerator;

@RequiredArgsConstructor
public class JwtVerifyFilter extends OncePerRequestFilter {

    private final JwtGenerator jwtGenerator;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String token = jwtGenerator.getToken(request);
        setAuthenticateIfTokenIsValid(token);

        filterChain.doFilter(request, response);

        SecurityContextHolder.clearContext();
    }

    private void setAuthenticateIfTokenIsValid(final String token) {
        if (token == null || !jwtGenerator.isValid(token)) {
            return;
        }

        Authentication authentication = jwtGenerator.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
