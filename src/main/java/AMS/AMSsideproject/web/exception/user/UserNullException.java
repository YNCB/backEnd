package AMS.AMSsideproject.web.exception.user;

public class UserNullException extends RuntimeException{

    public UserNullException() {
        super();
    }

    public UserNullException(String message){
        super(message);
    }
}
