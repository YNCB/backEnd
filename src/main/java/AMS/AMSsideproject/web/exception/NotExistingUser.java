package AMS.AMSsideproject.web.exception;

public class NotExistingUser extends RuntimeException{
    public NotExistingUser(String message) {
        super(message);
    }
}
