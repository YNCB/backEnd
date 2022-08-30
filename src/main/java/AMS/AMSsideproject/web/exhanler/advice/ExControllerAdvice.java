package AMS.AMSsideproject.web.exhanler.advice;


import AMS.AMSsideproject.web.exception.DuplicationUserNickname;
import AMS.AMSsideproject.web.exception.RefreshTokenExpireException;
import AMS.AMSsideproject.web.exception.RefreshTokenInvalidException;
import AMS.AMSsideproject.web.exception.UserNullException;
import AMS.AMSsideproject.web.exhanler.ErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExControllerAdvice {

    /**
     * user 관련
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNullException.class)
    public ErrorResult UserNullException(UserNullException e) {
        return new ErrorResult(e.getMessage(), "BAD", "400");
    }


    /**
     * refreshToken 관련
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RefreshTokenExpireException.class)
    public ErrorResult refreshTokenExpireException(RefreshTokenExpireException e) {
        return new ErrorResult(e.getMessage(), "BAD", "400");
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RefreshTokenInvalidException.class)
    public ErrorResult refreshTokenInvalidException(RefreshTokenInvalidException e) {
        return new ErrorResult(e.getMessage(), "BAD", "400");
    }

}
