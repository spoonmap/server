package xyz.spoonmap.server.member.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import xyz.spoonmap.server.annotation.EmailValidation;

public record SignupResponse(

    @JsonIgnore
    Long id,

    @EmailValidation
    String email

) {
}
