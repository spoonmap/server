package xyz.spoonmap.server.member.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import xyz.spoonmap.server.annotation.EmailValidation;

public record SignupRequest(
    @EmailValidation
    String email,

    @NotBlank
    @Size(min = 50, max = 60)
    String password,

    @NotBlank
    @Size(min = 2, max = 10)
    String nickname,

    @NotBlank
    @Size(min = 2, max = 10)
    String name
) {

}
