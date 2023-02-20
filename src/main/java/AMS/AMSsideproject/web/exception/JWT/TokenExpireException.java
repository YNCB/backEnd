package AMS.AMSsideproject.web.exception.JWT;

public class TokenExpireException extends RuntimeException{

    public TokenExpireException() {
        super();
    }

    public TokenExpireException(String message){
        super(message);
    }
}
