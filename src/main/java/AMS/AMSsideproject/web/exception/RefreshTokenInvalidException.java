package AMS.AMSsideproject.web.exception;

public class RefreshTokenInvalidException extends RuntimeException{

    public RefreshTokenInvalidException() {
        super();
    }

    public RefreshTokenInvalidException(String message){
        super(message);
    }
}
