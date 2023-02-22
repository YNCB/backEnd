package AMS.AMSsideproject.web.exhandler.advice;


import AMS.AMSsideproject.web.apiController.user.UserController;
import AMS.AMSsideproject.web.apiController.user.requestDto.ValidNickNameDto;
import AMS.AMSsideproject.web.exception.AlreadyJoinedUser;
import AMS.AMSsideproject.web.exception.DuplicationUserNickname;
import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
import AMS.AMSsideproject.web.exhandler.dto.DataErrorResult;
import AMS.AMSsideproject.web.exhandler.dto.UserValidExceptionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice(basePackageClasses = UserController.class)
public class UserExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicationUserNickname.class)
    public DataErrorResult<ValidNickNameDto> DuplicationUserNickname(DuplicationUserNickname e) {
        return new DataErrorResult(e.getMessage(), "BAD", "400",new ValidNickNameDto(e.getNickname()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AlreadyJoinedUser.class)
    public BaseErrorResult AlreadyJoinedUser(AlreadyJoinedUser e) {
        return new BaseErrorResult(e.getMessage(), "400", "BAD");
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
