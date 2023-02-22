package AMS.AMSsideproject.web.exhandler.advice;

import AMS.AMSsideproject.web.apiController.reply.ReplyController;
import AMS.AMSsideproject.web.exception.NotExistingPost;
import AMS.AMSsideproject.web.exception.NotExistingReply;
import AMS.AMSsideproject.web.exception.NotUserEq;
import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
import AMS.AMSsideproject.web.exhandler.dto.DataErrorResult;
import AMS.AMSsideproject.web.exhandler.dto.UserValidExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice(basePackageClasses = ReplyController.class)
public class ReplyExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotExistingPost.class)
    public BaseErrorResult NotExistingPost(NotExistingPost e) {
        return new BaseErrorResult(e.getMessage(),"400", "BAS_REQUEST");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotExistingReply.class)
    public BaseErrorResult NotExistingReply(NotExistingReply e) {
        return new BaseErrorResult(e.getMessage(),"400", "BAS_REQUEST");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(NotUserEq.class)
    public BaseErrorResult NotUserEq(NotUserEq e) {
        return new BaseErrorResult(e.getMessage(), "403", "FORBIDDEN");
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
