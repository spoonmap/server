package xyz.spoonmap.server.util.password;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class BcryptPasswordEncoder implements PasswordEncoder {

    private static final int COST = 12;

    @Override
    public String encode(CharSequence rawPassword) {
        return BCrypt.withDefaults().hashToString(COST, rawPassword.toString().toCharArray());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return BCrypt.verifyer()
                     .verify(rawPassword.toString().toCharArray(), encodedPassword)
                     .verified;
    }

}
