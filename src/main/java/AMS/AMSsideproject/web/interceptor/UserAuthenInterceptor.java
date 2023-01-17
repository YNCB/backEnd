package AMS.AMSsideproject.web.interceptor;

import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.custom.annotation.UserAuthen;
import AMS.AMSsideproject.web.exception.BlackJWTToken;
import AMS.AMSsideproject.web.exception.ExpireJWTTokenException;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class UserAuthenInterceptor implements HandlerInterceptor {

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
        UserAuthen addAuthRequired = handlerMethod.getMethodAnnotation(UserAuthen.class);
        if(Objects.isNull(addAuthRequired)) {
            return true;
        }

        //인증 체크(JWT 토큰)
        String token = jwtProvider.validAccessTokenHeader(request);
        String message = null;
        try {
            //header 에서 JWT 토큰이 있는지 검사
            if(!StringUtils.hasText(token))  //토큰이 없는 경우
                throw new Exception();

            System.out.println("111111111111111111111111");

            //로그아웃된 토큰인지 검사
            validBlackToken(token);

            System.out.println("222222222222222222222222");
            //JWT 토큰 만료기간 검증
            jwtProvider.validTokenExpired(token);

            System.out.println("33333333333333333");
            /**
             * refreshToken이 탈취당하였을때 refreshToken을 accessToken인척 사용할수 있기 때문에 구분해주기 위해서!!!
             */
            if(!jwtProvider.validTokenHeaderUser(token))
                throw new Exception();

            System.out.println("444444444444444444");
            return true;

        }catch (ExpireJWTTokenException e) {
            message = "엑세스 토큰의 유효기간이 만료되었습니다. 리프레쉬 토큰을 요청해주십시오.";
            sendRefreshResponse(message, response);
            return false;
        }catch (BlackJWTToken e) {
            sendBlackTokenResponse(e.getMessage(), response);
            return false;
        } catch (Exception e ) {
            message = "토큰이 없거나 정상적인 값이 아닙니다.";
            sendErrorResponse(e.getMessage(), response);
            return false;
        }
    }

    private void validBlackToken(String accessToken) {

        //Redis에 있는 엑세스 토큰인 경우 로그아웃 처리된 엑세스 토큰임.
        String blackToken = redisTemplate.opsForValue().get(accessToken);
        if(StringUtils.hasText(blackToken))
            throw new BlackJWTToken("로그아웃 처리된 엑세스 토큰입니다.");
    }

    private void sendBlackTokenResponse(String message, HttpServletResponse response) throws IOException {
        BaseErrorResult baseResponse =
                new BaseErrorResult(message, String.valueOf(HttpStatus.PRECONDITION_FAILED.value()), HttpStatus.PRECONDITION_FAILED.getReasonPhrase());
        String res = objectMapper.writeValueAsString(baseResponse);

        response.setStatus(HttpStatus.PRECONDITION_FAILED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }

    private void sendErrorResponse(String message, HttpServletResponse response) throws IOException {
        BaseErrorResult errorResult = new BaseErrorResult(message,"401", "Unauthorized");
        String res = objectMapper.writeValueAsString(errorResult);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }

    private void sendRefreshResponse(String message, HttpServletResponse response) throws IOException {
        BaseResponse baseResponse = new BaseResponse("201", message);
        String res = objectMapper.writeValueAsString(baseResponse);

        response.setStatus(HttpStatus.CREATED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }
}
