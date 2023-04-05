package xyz.spoonmap.server.member.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record PasswordUpdateResponse(
    String email,

    @JsonIgnore
    String updatedPassword
) {
}
