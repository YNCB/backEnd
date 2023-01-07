package AMS.AMSsideproject.web.exception;

public class BlackJWTToken extends RuntimeException{

    public BlackJWTToken(String message){
        super(message);
    }
}
