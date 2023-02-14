package AMS.AMSsideproject.web.exhandler.advice;

import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
import AMS.AMSsideproject.web.exhandler.dto.DataErrorResult;
import AMS.AMSsideproject.web.exhandler.advice.user.UserValidExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

//Controller 공통 에러 처리 핸들러
@RestControllerAdvice(basePackages = "web.apiController")
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseErrorResult EtcException(Exception e) {
        return new BaseErrorResult(e.getMessage(), "500", "INTERNAL_SERVER_ERROR");
    }

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

}
