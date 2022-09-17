package AMS.AMSsideproject.web.interceptor;

import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserJWTInterceptor implements HandlerInterceptor {

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

        //엑세스 토큰 가져옴
        String accessToken = request.getHeader(JwtProperties.HEADER_STRING);



        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
