package xyz.spoonmap.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.spoonmap.server.util.password.BcryptPasswordEncoder;
import xyz.spoonmap.server.util.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BcryptPasswordEncoder();
    }

}
