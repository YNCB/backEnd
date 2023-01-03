package AMS.AMSsideproject.web.exhandler.advice.follow;

import AMS.AMSsideproject.web.apiController.follow.FollowController;
import AMS.AMSsideproject.web.exception.NotExistingUser;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = FollowController.class)
public class FollowExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotExistingUser.class)
    public BaseErrorResult NotExistingUser(NotExistingUser e) {
        return new BaseErrorResult(e.getMessage(),"400", "BAD_REQUEST");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseErrorResult EtcException(Exception e) {
        return new BaseErrorResult(e.getMessage(), "500", "INTERNAL_SERVER_ERROR");
    }

}
