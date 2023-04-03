package xyz.spoonmap.server.exception.category;

public class CategoryNotFoundException extends IllegalStateException {
    private static final String MESSAGE = "카테고리를 찾을 수 없습니다.";

    public CategoryNotFoundException() {
        super(MESSAGE);
    }
}
