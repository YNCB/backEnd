package AMS.AMSsideproject.web.exhandler.advice;

import AMS.AMSsideproject.web.apiController.follow.FollowController;
import AMS.AMSsideproject.web.exception.AlreadyExistingFollow;
import AMS.AMSsideproject.web.exception.NotExistingFollow;
import AMS.AMSsideproject.web.exception.NotExistingUser;
import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
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

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyExistingFollow.class)
    public BaseErrorResult AlreadyExistingFollow(AlreadyExistingFollow e) {

        return new BaseErrorResult(e.getMessage(), "400", "BAD_REQUEST");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotExistingFollow.class)
    public BaseErrorResult NotExistingFollow(NotExistingFollow e) {
        return new BaseErrorResult(e.getMessage(), "400", "BAD_REQUEST");
    }

}
