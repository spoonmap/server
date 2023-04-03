package xyz.spoonmap.server.exception.post;

public class PostNotFoundException extends IllegalStateException {
    public static final String MESSAGE = "게시물을 찾을 수 없습니다.";

    public PostNotFoundException() {
        super(MESSAGE);
    }
}
