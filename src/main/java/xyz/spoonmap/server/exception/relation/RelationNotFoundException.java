package xyz.spoonmap.server.exception.relation;

import antlr.debug.MessageEvent;

public class RelationNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "팔로우 정보를 찾을 수 없습니다.";

    public RelationNotFoundException() {
        super(MESSAGE);
    }
}
