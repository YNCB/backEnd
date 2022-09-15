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
    @ExceptionHandler(DuplicationUserNickname.class)
    public ErrorResult DuplicationUserNickname(DuplicationUserNickname e) {
        return new ErrorResult(e.getMessage(), "BAD", "400");
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(UserNullException.class)
//    public ErrorResult UserNullException(UserNullException e) {
//        return new ErrorResult(e.getMessage(), "BAD", "400");
//    }



    /**
     * refreshToken 관련
     */
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(JWTTokenExpireException.class)
//    public ErrorResult refreshTokenExpireException(JWTTokenExpireException e) {
//        return new ErrorResult("리프레시 토큰이 만료되었습니다. 다시 로그인을 해주시기 바랍니다", "BAD", "400");
//    }
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(RefreshTokenInvalidException.class)
//    public ErrorResult refreshTokenInvalidException(RefreshTokenInvalidException e) {
//        return new ErrorResult(e.getMessage(), "BAD", "400");
//    }

}
