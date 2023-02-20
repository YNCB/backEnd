package AMS.AMSsideproject.web.exhandler.advice;

import AMS.AMSsideproject.web.exception.JWT.AuthorizationException;
import AMS.AMSsideproject.web.exception.JWT.TokenExistingException;
import AMS.AMSsideproject.web.exception.JWT.TokenExpireException;
import AMS.AMSsideproject.web.exception.JWT.TokenValidException;
import AMS.AMSsideproject.web.exception.UserNullException;
import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthInterceptorAdvice {

    //엑세스 유효성 에러
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenValidException.class)
    public BaseErrorResult JwtValidException(TokenValidException e) {
        return new BaseErrorResult("인증에 실패하였습니다.",String.valueOf(HttpStatus.UNAUTHORIZED.value()),HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }





    //토큰 기한 만료
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenExpireException.class)
    public BaseErrorResult JwtExpireException(TokenExpireException e) {
        return new BaseErrorResult("토큰의 기한이 만료되었습니다.",String.valueOf(HttpStatus.UNAUTHORIZED.value()),HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    //권한 없음
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationException.class)
    public BaseErrorResult AuthorizationException(AuthorizationException e){
        return new BaseErrorResult(e.getMessage(),String.valueOf(HttpStatus.FORBIDDEN.value()), HttpStatus.FOUND.getReasonPhrase());
    }

    //리프레쉬 인터셉터에서 발생
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNullException.class)
    public BaseErrorResult UserNullException(UserNullException e) {
        return new BaseErrorResult(e.getMessage(),String.valueOf(HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST.getReasonPhrase());
    }

    //리프레쉬 인터셉터에서 발생
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ExceptionHandler(TokenExistingException.class)
    public BaseErrorResult JwtExistingException(TokenExistingException e ) {
        return new BaseErrorResult(e.getMessage(), String.valueOf(HttpStatus.PRECONDITION_FAILED.value()), HttpStatus.PRECONDITION_FAILED.getReasonPhrase());
    }
}
