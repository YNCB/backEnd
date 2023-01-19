package AMS.AMSsideproject.web.exception;

public class NotEqRefreshToken extends RuntimeException{

    public NotEqRefreshToken() {
        super();
    }

    public NotEqRefreshToken(String message){
        super(message);
    }
}
