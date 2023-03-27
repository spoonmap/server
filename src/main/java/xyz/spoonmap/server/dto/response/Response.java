package xyz.spoonmap.server.dto.response;

import lombok.Getter;

@Getter
public class Response<T> {

    private Integer code;
    private T data;

}
