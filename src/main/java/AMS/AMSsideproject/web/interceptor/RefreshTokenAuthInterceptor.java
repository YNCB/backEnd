package AMS.AMSsideproject.web.interceptor;

import AMS.AMSsideproject.domain.refreshToken.service.RefreshTokenService;
import AMS.AMSsideproject.web.exception.JWT.TokenValidException;
import AMS.AMSsideproject.web.jwt.JwtProperties;
import AMS.AMSsideproject.web.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.exception.JWT.TokenExistingException;
import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static AMS.AMSsideproject.web.jwt.JwtProperties.ACCESS_PREFIX_STRING;

@RequiredArgsConstructor
public class RefreshTokenAuthInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //1. Request Header 토큰 추출
        String token = getToken(request);

        //2. 리프레쉬 토큰 유효성 검사
        if(!jwtProvider.validationToken(token))
            throw new TokenValidException();

        //3. 리프레쉬 토큰에서 Claims 검증 -> 위에서 유효성 검사를 했기 때문에 Claims 를 가져오는데는 오류가 발생하지 않음
        Claims claims = jwtProvider.parseClaims(token);
        if(claims.get("userId")==null)
            throw new TokenValidException();
        Long userId = Long.valueOf(claims.get("userId").toString());

        //4. DB의 리프레쉬 토큰과 일치하는지 확인
        refreshTokenService.validRefreshTokenValue(userId, token);

        return true;
    }

    //Request Header 에서 토큰 추출
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(jwtProvider.REFRESH_HEADER_STRING);
        if(!StringUtils.hasText(token))
            throw new TokenValidException();

        return token;
    }
}
