package AMS.AMSsideproject.web.exception;

public class NotValidToken extends RuntimeException{
    public NotValidToken(String message) {
        super(message);
    }
}
