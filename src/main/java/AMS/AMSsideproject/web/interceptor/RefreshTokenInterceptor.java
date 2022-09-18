package AMS.AMSsideproject.web.interceptor;

import AMS.AMSsideproject.domain.token.service.RefreshTokenService;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.exception.JWTTokenExpireException;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RefreshTokenInterceptor implements HandlerInterceptor {

    @Autowired private  JwtProvider jwtProvider;
    @Autowired private  RefreshTokenService refreshTokenService;
    @Autowired private  ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /**
         * 400 에러 어차피 한번에 다묶어써 공통 메세지로 처리하는게 좋지않나?!!!!!!!!!!!!!! !!!!!!!!
         * 그리고 리프레시 토큰 에러하고 만료하고 따로 처리해야되나?!(상태코드 다르게해서) -> 근데 결국 다시 로그인인데 굳이?!!!!!!!!!!
         */
        BaseErrorResult errorResult = null;

        //토큰이 정상적이 값인지 판별
        String refreshToken = jwtProvider.validRefreshTokenHeader(request);

        if(!StringUtils.hasText(refreshToken)) //헤더에 리프레시 토큰이 없는 경우
            errorResult = new BaseErrorResult("리프레시 토큰이 없거나 잘못된 값입니다.","BAD","400");

       //헤더에 리프레시 토큰이 있는 경우
        try{
            //토큰 유효기간 검증
            jwtProvider.validTokenExpired(refreshToken);

            //토큰에서 userId 추출
            Long findUserId = jwtProvider.getUserIdToToken(refreshToken);

            //요청한 사용자의 refreshToken 과 일치하는지 검증
            refreshTokenService.validRefreshTokenValue(findUserId, refreshToken);

            //request.setAttribute("userId", findUserId); //컨트롤러에 userId 전달
            return true;

        }catch (JWTTokenExpireException e) { //토큰 유효기간이 만료된 경우
            errorResult = new BaseErrorResult("리프레시토큰이 만료되었습니다. 다시 로그인 해주시기 바랍니다.", "BAD", "400");
        }catch (Exception e) { //토큰 정보가 정상적이지 않은 경우
            errorResult = new BaseErrorResult("리프레시 토큰이 없거나 잘못된 값입니다. 다시 로그인을 해주시기 바랍니다.","BAD","400");
        }

        // 정상적이지 않은 경우
        String res = objectMapper.writeValueAsString(errorResult);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
        return false;
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
