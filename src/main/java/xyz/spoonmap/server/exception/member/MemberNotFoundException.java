package xyz.spoonmap.server.exception.member;

public class MemberNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "회원을 찾을 수 없습니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }

}
