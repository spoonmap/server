package xyz.spoonmap.server.exception.domain.member;

public class MemberNotVerifiedException extends RuntimeException {

    private static final String MESSAGE = "인증되지 않은 회원입니다.";

    public MemberNotVerifiedException() {
        super(MESSAGE);
    }
}
