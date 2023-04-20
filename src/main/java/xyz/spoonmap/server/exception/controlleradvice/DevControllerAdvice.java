package xyz.spoonmap.server.exception.controlleradvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Profile("dev")
@RestControllerAdvice
public class DevControllerAdvice {

    @ExceptionHandler(Throwable.class)
    public void loggingInDevEnv(Throwable t) {
        t.printStackTrace();
    }

}
