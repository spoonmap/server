package xyz.spoonmap.server.exception.domain.relation;

public class FollowExistException extends IllegalArgumentException {

    private static final String MESSAGE = "이미 팔로우 중 입니다.";

    public FollowExistException() {
        super(MESSAGE);
    }

}
