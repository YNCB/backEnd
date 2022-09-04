package AMS.AMSsideproject.web.custom.security.filter;

import AMS.AMSsideproject.web.exhanler.ErrorResult;
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

        /**
         * 여기서 로그인에 대한 오류 처리!!!!! - 구분해서 처리해야되는거 아냐?!!!!!
         * ex) 회원가입하지 않은 회원
         * ex) 아이디, 패스워드가 틀린 사용자
         */


        ErrorResult errorResult = new ErrorResult("회원가입이 안된 사용자입니다. 회원가입을 해주시기 바랍니다.", "BAD", "400");
        String res = objectMapper.writeValueAsString(errorResult);

        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }
}
