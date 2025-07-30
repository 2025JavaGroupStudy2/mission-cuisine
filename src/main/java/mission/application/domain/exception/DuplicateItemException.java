package mission.application.domain.exception;

public class DuplicateItemException extends RuntimeException{
    public DuplicateItemException(){
        super("중복된 항목입니다.");
    }
}
