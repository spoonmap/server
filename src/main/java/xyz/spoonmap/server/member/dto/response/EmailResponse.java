package xyz.spoonmap.server.member.dto.response;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record EmailResponse(

    @NotBlank
    @Size(max = 500)
    @Email
    String email

) {
}
