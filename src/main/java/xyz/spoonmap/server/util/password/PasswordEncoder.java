package xyz.spoonmap.server.util.password;

import java.util.UUID;

public interface PasswordEncoder {

    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);

    default String generateRawPassword() {
        String[] uuid = UUID.randomUUID().toString().split("-");
        return uuid[0] + uuid[1];
    }

}
