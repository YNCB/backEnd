package AMS.AMSsideproject.web.custom.security.filter;

import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserLoginFailureCustomHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        //이거는 세세하게(회원가입하지 않은 사용자, 비밀번호 틀린사용자로 나눌때 사용하는 법!!!!!
//        if(exp.equals(BadCredentialsException.class)) { //비밀번호가 맞지 않았을 경우
//            errorResult = new ErrorResult(exception.getMessage(), "BAD", "400");
//
//        }else if(exp.equals(LoginUserNullException.class)) {
//            errorResult = new ErrorResult(exception.getMessage(), "BAD", "400");
//        }else {
//            errorResult = new ErrorResult("InternalAuthenticationError", "BAD", "400");
//        }

        BaseErrorResult errorResult = new BaseErrorResult(exception.getMessage(),"BAD", "400");
        String res = objectMapper.writeValueAsString(errorResult);

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }
}
