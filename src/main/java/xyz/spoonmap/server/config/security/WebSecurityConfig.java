package xyz.spoonmap.server.config.security;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xyz.spoonmap.server.authentication.JwtGenerator;
import xyz.spoonmap.server.authentication.filter.AuthenticateFilter;
import xyz.spoonmap.server.authentication.filter.JwtVerifyFilter;
import xyz.spoonmap.server.dto.response.ErrorResponse;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final ObjectMapper om;
    private final JwtGenerator jwtGenerator;
    private final AuthenticationConfiguration configuration;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // TODO: CORS 설정 해야함

        http
            .addFilterBefore(jwtAuthenticateFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtVerifyFilter(), UsernamePasswordAuthenticationFilter.class);

        http.anonymous().disable()
            .authorizeRequests()
            .antMatchers(Path.V1_USER).hasRole("USER")
            .antMatchers(Path.PERMIT_ALL).permitAll()
            .anyRequest().permitAll();

        http
            .exceptionHandling()
            .authenticationEntryPoint(((request, response, authException) -> {
                response.setCharacterEncoding(UTF_8.name());
                response.setStatus(FORBIDDEN.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                String msg = om.writeValueAsString(ErrorResponse.fail(FORBIDDEN.value(), "로그인이 필요합니다."));
                response.getWriter().write(msg);
                response.getWriter().flush();
            }))
            .accessDeniedHandler(((request, response, accessDeniedException) -> {
                response.setCharacterEncoding(UTF_8.name());
                response.setStatus(UNAUTHORIZED.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                String msg = om.writeValueAsString(ErrorResponse.fail(UNAUTHORIZED.value(), "권한이 없습니다."));
                response.getWriter().write(msg);
                response.getWriter().flush();
            }));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        return factory -> factory.addContextCustomizers(
            context -> context.setCookieProcessor(new LegacyCookieProcessor()));
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                         .antMatchers("/swagger*", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs")
                         .antMatchers("/h2-console/**")
                         .antMatchers("/health-check");
    }

    /**
     * @see <a href="https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/authentication/configuration/AuthenticationConfiguration.html">AuthenticationConfiguration</a>
     */
    private AuthenticateFilter jwtAuthenticateFilter() throws Exception {
        AuthenticateFilter filter =
            new AuthenticateFilter(configuration.getAuthenticationManager(), jwtGenerator);
        filter.setFilterProcessesUrl("/v1/members/login");

        return filter;
    }

    private JwtVerifyFilter jwtVerifyFilter() {
        return new JwtVerifyFilter(jwtGenerator);
    }

}
