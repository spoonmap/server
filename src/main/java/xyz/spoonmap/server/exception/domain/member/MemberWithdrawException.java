package xyz.spoonmap.server.exception.domain.member;

public class MemberWithdrawException extends IllegalArgumentException {

    private static final String MESSAGE = "탈퇴한 회원입니다.";

    public MemberWithdrawException() {
        super(MESSAGE);
    }
}
