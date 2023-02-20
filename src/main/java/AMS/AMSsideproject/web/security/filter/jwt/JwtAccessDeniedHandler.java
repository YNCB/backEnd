package AMS.AMSsideproject.web.security.filter.jwt;

import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
//Spring Security 에서 인증이 되었지만 권한이 없는 사용자의 리소스에 대한 접근 처리는 AccessDeniedHandler 가 담당
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.error("권한이 없는 사용자 접근");

        //response
        BaseErrorResult errorResult = new BaseErrorResult("권한이 없습니다.",
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                String.valueOf(HttpStatus.FORBIDDEN.value()));
        String res = objectMapper.writeValueAsString(errorResult);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }
}
