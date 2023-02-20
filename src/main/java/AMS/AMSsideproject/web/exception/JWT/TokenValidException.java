package AMS.AMSsideproject.web.exception.JWT;

public class TokenValidException extends RuntimeException{
    public TokenValidException(String message) {
        super(message);
    }

    public TokenValidException() {
    }
}
