package AMS.AMSsideproject.web.interceptor;

import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.custom.annotation.LoginAuthRequired;
import AMS.AMSsideproject.web.custom.annotation.VerityUserType;
import AMS.AMSsideproject.web.exception.user.JWTTokenExpireException;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class VerityUserInterceptor implements HandlerInterceptor {

    @Autowired private JwtProvider jwtProvider;
    @Autowired private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //애노테이션이 있는 uri 대상으로만
        if(!(handler instanceof HandlerMethod)){
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        VerityUserType addAuthRequired = handlerMethod.getMethodAnnotation(VerityUserType.class);
        if(Objects.isNull(addAuthRequired)) {
            return true;
        }

        String token = jwtProvider.validAccessTokenHeader(request); //앞에 인터셉터에서 기본 토큰 검사했으니
        String nickName = jwtProvider.getNickNameToToken(token);

        //Request URI 에서의 사용자 닉네임과 게시물 작성 요청한 사용자의 닉네임 동일 여부 확인
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        String RequestURI_nickname = split[2];

        Boolean verity = false;
        if(nickName.equals(RequestURI_nickname)) { //내페이지에 접속한 경우
            verity = true;
            request.setAttribute("verity", verity);
        }else { //다른 유저 페이지에 접속한 경우
            request.setAttribute("verity", verity);
        }

       return true;
    }

}
