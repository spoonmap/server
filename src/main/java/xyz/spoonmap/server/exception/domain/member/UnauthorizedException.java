package xyz.spoonmap.server.exception.domain.member;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {

    private static final String MESSAGE = "인증되지 않은 사용자입니다.";

    public UnauthorizedException() {
        super(MESSAGE);
    }
}
