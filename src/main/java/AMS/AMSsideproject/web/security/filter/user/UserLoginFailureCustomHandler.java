package AMS.AMSsideproject.web.security.filter.user;

import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//"UsernamePasswordCustomFilter" 에서 로그인 실패시 실행되는 커스텀 Handler
@Slf4j
@Component
@RequiredArgsConstructor
public class UserLoginFailureCustomHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        log.error("로그인 실패");

        //response
        BaseErrorResult errorResult = new BaseErrorResult("아이디 또는 비밀번호를 다시 확인해주시기 바랍니다.",
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        String res = objectMapper.writeValueAsString(errorResult);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }
}
