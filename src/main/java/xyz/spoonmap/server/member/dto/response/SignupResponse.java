package xyz.spoonmap.server.member.dto.response;

import xyz.spoonmap.server.annotation.EmailValidation;

public record SignupResponse(

    @EmailValidation
    String email

) {
}
