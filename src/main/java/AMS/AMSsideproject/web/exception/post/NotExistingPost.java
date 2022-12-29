package AMS.AMSsideproject.web.exception.post;

public class NotExistingPost extends RuntimeException{

    public NotExistingPost(String message) {
        super(message);
    }
}
