package AMS.AMSsideproject.web.exhandler.advice.user;


import AMS.AMSsideproject.web.apiController.user.UserController;
import AMS.AMSsideproject.web.apiController.user.requestDto.ValidNickNameDto;
import AMS.AMSsideproject.web.exception.AlreadyJoinedUser;
import AMS.AMSsideproject.web.exception.DuplicationUserNickname;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.exhandler.DataErrorResult;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

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

}
