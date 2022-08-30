package AMS.AMSsideproject.web.exception;

public class DuplicationUserNickname extends RuntimeException{

    public DuplicationUserNickname() {
        super();
    }

    public DuplicationUserNickname(String message){
        super(message);
    }
}
