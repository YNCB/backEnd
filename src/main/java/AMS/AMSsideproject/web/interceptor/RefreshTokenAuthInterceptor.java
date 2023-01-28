package AMS.AMSsideproject.web.interceptor;

import AMS.AMSsideproject.domain.refreshToken.service.RefreshTokenService;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.exception.JWT.JwtExpireException;
import AMS.AMSsideproject.web.exception.JWT.JwtValidException;
import AMS.AMSsideproject.web.exception.NotEqRefreshToken;
import AMS.AMSsideproject.web.exception.JWT.JwtExistingException;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RefreshTokenAuthInterceptor implements HandlerInterceptor {

    @Autowired private  JwtProvider jwtProvider;
    @Autowired private  RefreshTokenService refreshTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        BaseErrorResult errorResult = null;

        //header 에서 refresh 토큰이 있는지 검사
        String refreshToken = jwtProvider.validRefreshTokenHeader(request);
        if(!StringUtils.hasText(refreshToken))  //토큰이 없는 경우
            throw new JwtExistingException("토큰이 없습니다.");

        //토큰 유효성 검사
        jwtProvider.validateToken(refreshToken);

        //추가 인증 검사
        Long findUserId = jwtProvider.getUserId(refreshToken);
        refreshTokenService.validRefreshTokenValue(findUserId, refreshToken);

        return true;
    }

}
