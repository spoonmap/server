package xyz.spoonmap.server.member.enums;

import lombok.Getter;

public enum DuplicationType {

    EMAIL("이메일"), NICKNAME("닉네임");

    @Getter
    private final String type;

    DuplicationType(String type) {
        this.type = type;
    }

}
