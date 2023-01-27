package AMS.AMSsideproject.web.exception.JWT;

public class JwtExpireException extends RuntimeException{

    public JwtExpireException() {
        super();
    }

    public JwtExpireException(String message){
        super(message);
    }
}
