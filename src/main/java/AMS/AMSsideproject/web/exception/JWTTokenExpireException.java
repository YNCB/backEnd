package AMS.AMSsideproject.web.exception;

public class JWTTokenExpireException extends RuntimeException{

    public JWTTokenExpireException() {
        super();
    }

    public JWTTokenExpireException(String message){
        super(message);
    }
}
