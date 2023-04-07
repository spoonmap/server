package xyz.spoonmap.server.exception.domain.relation;

import xyz.spoonmap.server.exception.domain.common.NotFoundException;

public class RelationNotFoundException extends NotFoundException {

    private static final String MESSAGE = "팔로우 정보를 찾을 수 없습니다.";

    public RelationNotFoundException() {
        super(MESSAGE);
    }
}
