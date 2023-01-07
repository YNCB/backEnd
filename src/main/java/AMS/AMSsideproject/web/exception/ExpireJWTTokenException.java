package AMS.AMSsideproject.web.exception;

public class ExpireJWTTokenException extends RuntimeException{

    public ExpireJWTTokenException() {
        super();
    }

    public ExpireJWTTokenException(String message){
        super(message);
    }
}
