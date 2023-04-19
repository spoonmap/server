package xyz.spoonmap.server.exception.domain.notification;

public class InvalidNotificationTypeException extends RuntimeException {

    private static final String MESSAGE = "올바르지 않은 타입입니다.";

    public InvalidNotificationTypeException() {
        super(MESSAGE);
    }

}
