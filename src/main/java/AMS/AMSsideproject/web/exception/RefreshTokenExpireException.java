package AMS.AMSsideproject.web.exception;

public class RefreshTokenExpireException extends RuntimeException{

    public RefreshTokenExpireException() {
        super();
    }

    public RefreshTokenExpireException(String message){
        super(message);
    }
}
