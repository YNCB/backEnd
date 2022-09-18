package AMS.AMSsideproject.web.exception.user;

public class AlreadyJoinedUser extends RuntimeException{

    public AlreadyJoinedUser() {
        super();
    }

    public AlreadyJoinedUser(String message){
        super(message);
    }
}
