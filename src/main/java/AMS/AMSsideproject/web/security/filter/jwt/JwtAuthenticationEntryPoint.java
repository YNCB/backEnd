package AMS.AMSsideproject.web.security.filter.jwt;

import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//Spring Security 에서 인증되지 않은 사용자의 리소스에 대한 접근 처리는 AuthenticationEntryPoint 가 담당
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        log.error("JWT 토큰 인증 실패");

        //response
        BaseErrorResult errorResult = new BaseErrorResult("인증에 실패하였습니다.",
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        String res = objectMapper.writeValueAsString(errorResult);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }
}
