package AMS.AMSsideproject.web.exception;

public class RefreshTokenValidException extends RuntimeException{

    public RefreshTokenValidException() {
        super();
    }

    public RefreshTokenValidException(String message){
        super(message);
    }
}
