package xyz.spoonmap.server.exception.controlleradvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.spoonmap.server.dto.response.ErrorResponse;
import xyz.spoonmap.server.exception.domain.common.NotFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(NOT_FOUND)
                             .body(new ErrorResponse(NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleServerException(Exception e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), "오류가 발생했습니다."));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleOther(Throwable t) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), "오류가 발생했습니다."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponse> handleNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();

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
        } catch (NullPointerException e) {
            log.info("Field Error Null");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(new ErrorResponse(BAD_REQUEST.value(), builder.toString()));
    }

}
