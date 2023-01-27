package AMS.AMSsideproject.web.exception.JWT;

public class AuthorizationException extends RuntimeException{
    public AuthorizationException(String message) {
        super(message);
    }
}
