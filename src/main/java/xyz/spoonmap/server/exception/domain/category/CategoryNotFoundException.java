package xyz.spoonmap.server.exception.domain.category;

import xyz.spoonmap.server.exception.domain.common.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {

    private static final String MESSAGE = "카테고리를 찾을 수 없습니다.";

    public CategoryNotFoundException() {
        super(MESSAGE);
    }

}
