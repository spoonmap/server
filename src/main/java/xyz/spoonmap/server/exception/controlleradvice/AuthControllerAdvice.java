package xyz.spoonmap.server.exception.controlleradvice;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.spoonmap.server.dto.response.ErrorResponse;
import xyz.spoonmap.server.exception.domain.member.MemberWithdrawException;
import xyz.spoonmap.server.exception.domain.member.UnauthorizedException;

@Slf4j
@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(UNAUTHORIZED)
                             .body(new ErrorResponse(UNAUTHORIZED.value(), e.getMessage()));
    }

    @ExceptionHandler(MemberWithdrawException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(MemberWithdrawException e) {
        return ResponseEntity.status(UNAUTHORIZED)
                             .body(new ErrorResponse(UNAUTHORIZED.value(), e.getMessage()));
    }

}
