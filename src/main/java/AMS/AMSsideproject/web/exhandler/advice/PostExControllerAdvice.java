package AMS.AMSsideproject.web.exhandler.advice;

import AMS.AMSsideproject.web.apiController.like.LikeController;
import AMS.AMSsideproject.web.apiController.post.PostControllerV2;
import AMS.AMSsideproject.web.exception.NotExistingUser;
import AMS.AMSsideproject.web.exception.UserNullException;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = PostControllerV2.class)
public class PostExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNullException.class)
    public BaseErrorResult UserNullException(UserNullException e) {
        return new BaseErrorResult(e.getMessage(),"400", "BAS_REQUEST");
    }
}
