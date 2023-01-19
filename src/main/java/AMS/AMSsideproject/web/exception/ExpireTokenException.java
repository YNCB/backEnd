package AMS.AMSsideproject.web.exception;

public class ExpireTokenException extends RuntimeException{

    public ExpireTokenException() {
        super();
    }

    public ExpireTokenException(String message){
        super(message);
    }
}
