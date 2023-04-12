package xyz.spoonmap.server.config.security;

import static lombok.AccessLevel.PRIVATE;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class Path {

    static final String[] V1_USER = {
        // Members
        "/**/members/profile/**", "/**/members/follows", "/**/members/followers/**"

        // Posts

        // Comments

        // ...
    };

    static final String[] PERMIT_ALL = {
        "**/login", "**/signup", "/members/verify"
    };

}
