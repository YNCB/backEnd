package AMS.AMSsideproject.web.exhandler.advice;

import AMS.AMSsideproject.web.apiController.post.PostControllerV2;
import AMS.AMSsideproject.web.exception.JWT.AuthorizationException;
import AMS.AMSsideproject.web.exception.JWT.TokenExpireException;
import AMS.AMSsideproject.web.exception.JWT.TokenValidException;
import AMS.AMSsideproject.web.exception.UserNullException;
import AMS.AMSsideproject.web.exhandler.dto.UserValidExceptionDto;
import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
import AMS.AMSsideproject.web.exhandler.dto.DataErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice(basePackageClasses = PostControllerV2.class)
public class PostExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNullException.class)
    public BaseErrorResult UserNullException(UserNullException e) {
        return new BaseErrorResult(e.getMessage(),"400", "BAS_REQUEST");
    }


    //인증 관련
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


    //공통
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DataErrorResult<List> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<UserValidExceptionDto> result = new ArrayList<>();

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for(FieldError fieldError : fieldErrors) {
            UserValidExceptionDto dto = new UserValidExceptionDto(fieldError.getField(),fieldError.getRejectedValue(), fieldError.getDefaultMessage());
            result.add(dto);


            System.out.print(fieldError.getField() + "-> code: " + fieldError.getCode());
            for(String code: fieldError.getCodes()) {
                System.out.print(" codes: " + code);
            }
            System.out.println();
        }
        return new DataErrorResult<>("각 필드의 조건이 맞지않습니다.", "BAD","406", result);
    }
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseErrorResult EtcException(Exception e) {
        return new BaseErrorResult(e.getMessage(), "500", "INTERNAL_SERVER_ERROR");
    }
}
