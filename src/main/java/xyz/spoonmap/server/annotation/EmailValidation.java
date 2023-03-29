package xyz.spoonmap.server.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@NotBlank
@Size(min = 4, max = 500)
@Email
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface EmailValidation {
}
