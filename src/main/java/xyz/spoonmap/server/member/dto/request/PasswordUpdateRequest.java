package xyz.spoonmap.server.member.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record PasswordUpdateRequest(

    @NotBlank
    @Size(min = 8, max = 20)
    String password

) {
}
