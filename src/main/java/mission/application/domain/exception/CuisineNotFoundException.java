package mission.application.domain.exception;

public class CuisineNotFoundException extends RuntimeException{
    public CuisineNotFoundException(){
        super("존재하지 않는 요리입니다.");
    }
}
