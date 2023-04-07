package xyz.spoonmap.server.exception.controlleradvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.spoonmap.server.exception.domain.common.NotFoundException;
import xyz.spoonmap.server.dto.response.ErrorResponse;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        return ResponseEntity.status(NOT_FOUND)
                             .body(new ErrorResponse(NOT_FOUND.value(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleServerException(Exception e) {
        log.error(e.getMessage());
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), "오류가 발생했습니다."));
    }

}
