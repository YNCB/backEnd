package AMS.AMSsideproject.web.interceptor;

import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.custom.annotation.PostAuthor;
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
public class PostAuthInterceptor implements HandlerInterceptor {

    @Autowired private JwtProvider jwtProvider;
    @Autowired private ObjectMapper objectMapper;
    /**
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * 기본적인 JWT 토큰의 검증(request header 에 접두어 , jwt keySet, 기한)은 spring security filter 에서 다했음
     *
     * !고민할것
     * 회원수정할때만 사용될건데 회원수정 api에는 nickname이 없으니깐 uri의 nickname이랑 JWT의 nickname이랑 비교안해도 되는데
     * 그럼 해커들이 JWT 토큰값만 바꿔서 요청하면 다요청되는데 이건 막아야되는거 아냐?!!
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        PostAuthor addAuthRequired = handlerMethod.getMethodAnnotation(PostAuthor.class);
        if(Objects.isNull(addAuthRequired)) {
            return true;
        }

        //엑세스 토큰 가져옴
        String token = request.getHeader(JwtProperties.ACCESS_HEADER_STRING);
        String accessToken = jwtProvider.parsingAccessToken(token);

        //토큰에서 사용자 고유 아이디 추츨
        String findNickName = jwtProvider.getNickname(accessToken);

        //Request URI 에서의 사용자 닉네임과 게시물 작성 요청한 사용자의 닉네임 동일 여부 확인
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        String RequestURI_nickname = split[2];

        //일치하지 않는 경우
        if(!findNickName.equals(RequestURI_nickname)) {
            sendErrorResponse("권한이 없습니다.", response);
            return false;
        }

        return true;
    }

    private void sendErrorResponse(String message, HttpServletResponse response) throws IOException {
        BaseErrorResult errorResult = new BaseErrorResult(message,"BAD", "403");
        String res = objectMapper.writeValueAsString(errorResult);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }
}
