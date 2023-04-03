package xyz.spoonmap.server.photo.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record PhotoRequestDto(
        @NotNull
        @Size(min = 1, max = 255)
        String originName,

        @NotNull
        @Size(min = 3, max = 500)
        String url
) {

}
