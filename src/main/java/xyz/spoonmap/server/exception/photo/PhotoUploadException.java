package xyz.spoonmap.server.exception.photo;

public class PhotoUploadException extends RuntimeException {
    private static final String MESSAGE = "이미지 업로드에 실패했습니다.";

    public PhotoUploadException() {
        super(MESSAGE);
    }
}
