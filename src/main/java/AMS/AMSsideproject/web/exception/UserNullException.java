package AMS.AMSsideproject.web.exception;

public class UserNullException extends RuntimeException{

    public UserNullException() {
        super();
    }

    public UserNullException(String message){
        super(message);
    }
}
