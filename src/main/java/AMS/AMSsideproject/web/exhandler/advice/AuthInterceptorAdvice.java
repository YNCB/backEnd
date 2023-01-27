package AMS.AMSsideproject.web.exhandler.advice;

import AMS.AMSsideproject.web.exception.JWT.AuthorizationException;
import AMS.AMSsideproject.web.exception.JWT.JwtExpireException;
import AMS.AMSsideproject.web.exception.JWT.JwtValidException;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthInterceptorAdvice {

    //엑세스 유효성 에러
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtValidException.class)
    public BaseErrorResult JwtValidException(JwtValidException e) {
        return new BaseErrorResult("정상적이지 않은 토큰입니다.",String.valueOf(HttpStatus.UNAUTHORIZED.value()),HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    //엑세스 토큰 기한 만료
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtExpireException.class)
    public BaseErrorResult JwtExpireException(JwtExpireException e) {
        return new BaseErrorResult("엑세스 토큰의 기한이 만료되었습니다.",String.valueOf(HttpStatus.UNAUTHORIZED.value()),HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    //권한 없음
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationException.class)
    public BaseErrorResult AuthorizationException(AuthorizationException e){
        return new BaseErrorResult(e.getMessage(),String.valueOf(HttpStatus.FORBIDDEN.value()), HttpStatus.FOUND.getReasonPhrase());
    }
}
