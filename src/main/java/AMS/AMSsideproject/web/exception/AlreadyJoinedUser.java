package AMS.AMSsideproject.web.exception;

public class AlreadyJoinedUser extends RuntimeException{

    public AlreadyJoinedUser() {
        super();
    }

    public AlreadyJoinedUser(String message){
        super(message);
    }
}
