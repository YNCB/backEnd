package AMS.AMSsideproject.web.security.filter.jwt;

import AMS.AMSsideproject.web.jwt.JwtProperties;
import AMS.AMSsideproject.web.jwt.service.JwtProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//jwt 인증 처리 커스텀 필터
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        //1. Request Header 토큰 추출
        String accessToken = getToken(request);

        //2. 토큰 유효성 검사(헤더에 토큰이 있는지, 로그아웃된 토큰인지, 유효성 및 유효기간 검사)
        if(StringUtils.hasText(accessToken) && jwtProvider.validBlackToken(accessToken) && jwtProvider.validationToken(accessToken)){

            //3. 토큰으로 인증 정보 추출
            Authentication authentication = jwtProvider.getAuthentication(accessToken);

            if(authentication!=null) {
                //4. SecurityContext 에 저장 (인가검증에 사용)
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        //인증 실패시 SecurityContext 에 Authentication 객체가 없어 다음 필터에서 인증 실패 처리
        chain.doFilter(request,response);
    }

    //Request Header 에서 토큰 추출
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(jwtProvider.ACCESS_HEADER_STRING);
        if(StringUtils.hasText(token) && token.startsWith(jwtProvider.ACCESS_PREFIX_STRING))
            return token.substring(7);
        return null;
    }

}
