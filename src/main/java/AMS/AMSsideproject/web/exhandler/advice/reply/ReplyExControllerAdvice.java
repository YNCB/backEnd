package AMS.AMSsideproject.web.exhandler.advice.reply;

import AMS.AMSsideproject.web.apiController.reply.ReplyController;
import AMS.AMSsideproject.web.exception.post.NotExistingPost;
import AMS.AMSsideproject.web.exception.reply.NotExistingReply;
import AMS.AMSsideproject.web.exception.reply.NotUserEq;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = ReplyController.class)
public class ReplyExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotExistingPost.class)
    public BaseErrorResult NotExistingPost(NotExistingPost e) {
        return new BaseErrorResult(e.getMessage(),"400", "BAS_REQUEST");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotExistingReply.class)
    public BaseErrorResult NotExistingReply(NotExistingReply e) {
        return new BaseErrorResult(e.getMessage(),"400", "BAS_REQUEST");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NotUserEq.class)
    public BaseErrorResult NotUserEq(NotUserEq e) {
        return new BaseErrorResult(e.getMessage(), "403", "FORBIDDEN");
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseErrorResult EtcException(Exception e) {
        return new BaseErrorResult(e.getMessage(), "500", "INTERNAL_SERVER_ERROR");
    }


}
