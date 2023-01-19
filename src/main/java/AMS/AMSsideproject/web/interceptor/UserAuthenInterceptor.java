package AMS.AMSsideproject.web.interceptor;

import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.custom.annotation.UserAuthen;
import AMS.AMSsideproject.web.exception.BlackToken;
import AMS.AMSsideproject.web.exception.ExpireTokenException;
import AMS.AMSsideproject.web.exception.NotExistingToken;
import AMS.AMSsideproject.web.exception.NotValidToken;
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
                throw new NotExistingToken("토큰이 없습니다.");

            //로그아웃된 토큰인지 검사
            validBlackToken(token);

            //JWT 토큰 만료기간 검증
            jwtProvider.validTokenExpired(token);

            /**
             * refreshToken이 탈취당하였을때 refreshToken을 accessToken인척 사용할수 있기 때문에 구분해주기 위해서!!!
             */
            if(!jwtProvider.validTokenHeaderUser(token))
                throw new NotValidToken("정상적이지 않은 토큰입니다.");

            return true;

        }catch (ExpireTokenException e) { //기한만료된 토큰-201
            sendResponse(response, e.getMessage(),
                    HttpStatus.CREATED.value(), HttpStatus.CREATED.getReasonPhrase());
            return false;
        }catch (BlackToken e) { //로그아웃된 토큰-401
            sendResponse(response, e.getMessage(),
                    HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return false;
        } catch (NotExistingToken e){ //헤더에 토큰이 없는경우-412
            sendResponse(response, e.getMessage(),
                    HttpStatus.PRECONDITION_FAILED.value(),HttpStatus.PRECONDITION_FAILED.getReasonPhrase() );
            return false;
        }catch (NotValidToken e) { //정상적이지 않은 토큰-401
            sendResponse(response, e.getMessage(),
                    HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return false;
        }
        catch (Exception e) { //나머지 서버 에러
            sendResponse(response, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return false;
        }
    }

    private void validBlackToken(String accessToken) {

        //Redis에 있는 엑세스 토큰인 경우 로그아웃 처리된 엑세스 토큰임.
        String blackToken = redisTemplate.opsForValue().get(accessToken);
        if(StringUtils.hasText(blackToken))
            throw new BlackToken("로그아웃 처리된 엑세스 토큰입니다.");
    }

    private void sendResponse(HttpServletResponse response, String message, int code, String status ) throws IOException {

        BaseErrorResult result = new BaseErrorResult(message, String.valueOf(code), status);

        String res = objectMapper.writeValueAsString(result);
        response.setStatus(code);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }
}
