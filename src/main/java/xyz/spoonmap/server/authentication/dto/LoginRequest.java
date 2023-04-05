package xyz.spoonmap.server.authentication.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record LoginRequest(

    @Email
    @NotBlank
    @Size(max = 500)
    String email,

    @NotBlank
    @Size(min=8, max = 20)
    String password
) {
}
