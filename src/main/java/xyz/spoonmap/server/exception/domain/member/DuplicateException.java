package xyz.spoonmap.server.exception.domain.member;

import xyz.spoonmap.server.member.enums.DuplicationType;

public class DuplicateException extends RuntimeException {

    private static final String MESSAGE = "중복된 %s 입니다.";

    public DuplicateException(DuplicationType type) {
        super(String.format(MESSAGE, type.getType()));
    }

}
