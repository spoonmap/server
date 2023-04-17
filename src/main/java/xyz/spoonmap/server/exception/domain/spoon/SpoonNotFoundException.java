package xyz.spoonmap.server.exception.domain.spoon;

import xyz.spoonmap.server.exception.domain.common.NotFoundException;

public class SpoonNotFoundException extends NotFoundException {
    private static final String MESSAGE = "스푼을 찾을 수 없습니다.";

    public SpoonNotFoundException() {
        super(MESSAGE);
    }
}
