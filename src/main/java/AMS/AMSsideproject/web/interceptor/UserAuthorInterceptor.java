package AMS.AMSsideproject.web.interceptor;

import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.custom.annotation.UserAuthor;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class UserAuthorInterceptor implements HandlerInterceptor {

    @Autowired private JwtProvider jwtProvider;
    @Autowired private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //애노테이션이 있는 uri 대상으로만
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        UserAuthor addAuthRequired = handlerMethod.getMethodAnnotation(UserAuthor.class);
        if(Objects.isNull(addAuthRequired)) {
            return true;
        }

        //앞서 인증필터를 통해 토큰 유무,유효성 검사를 마쳤음.
        String accessToken = jwtProvider.validAccessTokenHeader(request);
        String role = jwtProvider.getRoleToToken(accessToken);

        try { //사용자 페이지에 접속할수 있는 권한을 가진 경우
            UserPageAuth userPage = UserPageAuth.valueOf(role);
            return true;
        }catch (IllegalArgumentException e ) { //사용자 페이지에 접속할수 있는 권한을 가지지 못한경우

            sendErrorResponse("권한이 없습니다", response);
            return false;
        }
    }
    private void sendErrorResponse(String message, HttpServletResponse response) throws IOException {
        BaseErrorResult errorResult = new BaseErrorResult(message,"403", "Forbidden");
        String res = objectMapper.writeValueAsString(errorResult);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }
}
