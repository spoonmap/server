package xyz.spoonmap.server.exception.controlleradvice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.spoonmap.server.dto.response.ErrorResponse;
import xyz.spoonmap.server.exception.domain.photo.PhotoUploadException;

@Slf4j
@RestControllerAdvice
public class PhotoRestControllerAdvice {

    @ExceptionHandler(PhotoUploadException.class)
    public ResponseEntity<ErrorResponse> handlePhotoUploadException(PhotoUploadException e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponse(INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

}
