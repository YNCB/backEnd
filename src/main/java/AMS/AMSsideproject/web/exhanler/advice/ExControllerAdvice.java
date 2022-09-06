package AMS.AMSsideproject.web.exhanler.advice;


import AMS.AMSsideproject.web.exception.*;
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
    @ExceptionHandler(DuplicationUserId.class)
    public ErrorResult DuplicationUserId(DuplicationUserId e) {
        return new ErrorResult(e.getMessage(), "BAD", "400");
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(DuplicationUserNickname.class)
    public ErrorResult DuplicationUserNickname(DuplicationUserNickname e) {
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
