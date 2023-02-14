package AMS.AMSsideproject.web.exhandler.advice.reply;

import AMS.AMSsideproject.web.apiController.reply.ReplyController;
import AMS.AMSsideproject.web.exception.NotExistingPost;
import AMS.AMSsideproject.web.exception.NotExistingReply;
import AMS.AMSsideproject.web.exception.NotUserEq;
import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
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

}
