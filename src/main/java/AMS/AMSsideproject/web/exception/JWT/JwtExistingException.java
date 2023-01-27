package AMS.AMSsideproject.web.exception.JWT;

public class JwtExistingException extends RuntimeException{
    public JwtExistingException(String message) {
        super(message);
    }
}
