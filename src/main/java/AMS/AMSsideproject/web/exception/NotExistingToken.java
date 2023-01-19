package AMS.AMSsideproject.web.exception;

public class NotExistingToken extends RuntimeException{
    public NotExistingToken(String message) {
        super(message);
    }
}
