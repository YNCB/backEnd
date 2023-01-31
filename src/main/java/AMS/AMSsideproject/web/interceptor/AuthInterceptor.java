package AMS.AMSsideproject.web.interceptor;

import AMS.AMSsideproject.domain.user.Role;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.custom.annotation.Auth;
import AMS.AMSsideproject.web.exception.JWT.AuthorizationException;
import AMS.AMSsideproject.web.exception.JWT.JwtValidException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class AuthInterceptor implements HandlerInterceptor {

    @Autowired private JwtProvider jwtProvider;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //애노테이션이 있는 uri 대상으로만
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Auth addAuthRequired = handlerMethod.getMethodAnnotation(Auth.class);
        if(Objects.isNull(addAuthRequired)) {
            return true;
        }

        //헤더에 토큰이 있는지 검사 -> 토큰이 있으면 토큰검사, 없을시 계속 진행
        String token = jwtProvider.validAccessTokenHeader(request);
        if(!StringUtils.hasText(token))
            return true;

        //token parsing
        String accessToken = jwtProvider.parsingAccessToken(token);

        /** 인증 검사 - 토큰 유효성 검사 **/
        //로그아웃 처리된 토큰인지 검사
        validBlackToken(accessToken);

        //토큰 유효성 검사
        jwtProvider.validateToken(accessToken);

        /** 권한 검사 **/
        String role = jwtProvider.getRole(accessToken);
        if(role.equals(Role.USER.name()) || role.equals(Role.MANAGER.name()) || role.equals(Role.ADMIN.name()))
            return true;

        throw new AuthorizationException("권한이 없습니다");
    }

    private void validBlackToken(String accessToken) {

        //Redis에 있는 엑세스 토큰인 경우 로그아웃 처리된 엑세스 토큰임.
        String blackToken = redisTemplate.opsForValue().get(accessToken);
        if(StringUtils.hasText(blackToken))
            throw new JwtValidException("로그아웃 처리된 엑세스 토큰입니다.");
    }
}
