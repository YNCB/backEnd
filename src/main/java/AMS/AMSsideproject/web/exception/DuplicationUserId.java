package AMS.AMSsideproject.web.exception;

public class DuplicationUserId extends RuntimeException{

    public DuplicationUserId() {
        super();
    }

    public DuplicationUserId(String message){
        super(message);
    }
}
