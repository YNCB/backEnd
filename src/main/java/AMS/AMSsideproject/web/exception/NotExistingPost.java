package AMS.AMSsideproject.web.exception;

public class NotExistingPost extends RuntimeException{

    public NotExistingPost(String message) {
        super(message);
    }
}
