package xyz.spoonmap.server.exception.controlleradvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.spoonmap.server.dto.response.ErrorResponse;
import xyz.spoonmap.server.exception.domain.common.NotFoundException;
import xyz.spoonmap.server.exception.domain.member.DuplicateException;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {

        return ErrorResponse.fail(NOT_FOUND, NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> handleDuplicationException(DuplicateException e) {

        return ErrorResponse.fail(BAD_REQUEST, BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleServerException(Exception e) {
        log.error("{}", e.getStackTrace()[0]);
        return ErrorResponse.fail(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.value(), "오류가 발생했습니다.");
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleOther(Throwable t) {
        log.error("{}", t.getStackTrace()[0]);
        return ErrorResponse.fail(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.value(), "오류가 발생했습니다.");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponse> handleNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();

        StringBuilder builder = new StringBuilder();
        builder.append("[Valid Error] ");

        try {
            if (bindingResult.hasErrors()) {
                builder.append("Reason: ")
                       .append(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage())
                       .append(System.lineSeparator())
                       .append("At: ")
                       .append(bindingResult.getObjectName())
                       .append(System.lineSeparator())
                       .append("Field: ")
                       .append(Objects.requireNonNull(bindingResult.getFieldError()).getField())
                       .append(System.lineSeparator())
                       .append("Not valid input: ")
                       .append(Objects.requireNonNull(bindingResult.getFieldError()).getRejectedValue())
                       .append(System.lineSeparator());
            }
        } catch (NullPointerException ne) {
            builder.append("Field Error Null");
        }

        log.error("{}", e.getStackTrace()[0]);
        return ErrorResponse.fail(BAD_REQUEST, BAD_REQUEST.value(), builder.toString());
    }

}
