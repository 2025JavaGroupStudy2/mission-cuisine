package mission.application.domain.exception;

public class CorruptedFileException extends RuntimeException {
    public CorruptedFileException() {
        super("파일을 읽는 도중 오류가 발생했습니다.");
    }
}
