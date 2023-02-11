package AMS.AMSsideproject.web.exhandler.advice.like;

import AMS.AMSsideproject.web.apiController.like.LikeController;
import AMS.AMSsideproject.web.apiController.reply.ReplyController;
import AMS.AMSsideproject.web.exception.NotExistingPost;
import AMS.AMSsideproject.web.exception.NotExistingReply;
import AMS.AMSsideproject.web.exception.NotExistingUser;
import AMS.AMSsideproject.web.exception.NotUserEq;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = LikeController.class)
public class LikeExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotExistingUser.class)
    public BaseErrorResult NotExistingUser(NotExistingUser e) {
        return new BaseErrorResult(e.getMessage(),"400", "BAS_REQUEST");
    }




}
