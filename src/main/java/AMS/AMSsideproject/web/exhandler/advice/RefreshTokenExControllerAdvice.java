package AMS.AMSsideproject.web.exhandler.advice;

import AMS.AMSsideproject.web.apiController.refreshToken.RefreshTokenController;
import AMS.AMSsideproject.web.exception.JWT.TokenValidException;
import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackageClasses = {RefreshTokenController.class})
public class RefreshTokenExControllerAdvice {

    //엑세스 유효성 에러
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenValidException.class)
    public BaseErrorResult JwtValidException(TokenValidException e) {
        return new BaseErrorResult("인증에 실패하였습니다.",String.valueOf(HttpStatus.UNAUTHORIZED.value()),HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }

    //공통
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseErrorResult EtcException(Exception e) {
        return new BaseErrorResult(e.getMessage(), "500", "INTERNAL_SERVER_ERROR");
    }

}
