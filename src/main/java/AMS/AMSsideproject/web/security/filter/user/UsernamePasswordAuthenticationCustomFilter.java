package AMS.AMSsideproject.web.security.filter.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//로그인 인증 처리 커스텀 필터
//@RequiredArgsConstructor
public class UsernamePasswordAuthenticationCustomFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final UserLoginSuccessCustomHandler successHandler;
    private final UserLoginFailureCustomHandler failureHandler;

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/codebox/login",
            "POST");

    public UsernamePasswordAuthenticationCustomFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper,
                                                      UserLoginSuccessCustomHandler userLoginSuccessCustomHandler,
                                                      UserLoginFailureCustomHandler userLoginFailureCustomHandler) {
       // super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        //setAuthenticationSuccessHandler(userLoginSuccessCustomHandler);
        //setAuthenticationFailureHandler(userLoginFailureCustomHandler);

        this.successHandler = userLoginSuccessCustomHandler;
        this.failureHandler = userLoginFailureCustomHandler;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //1. body 에서 로그인 정보 받아오기
        UserLoginRequestDto loginDto = null;
        try {
            loginDto = objectMapper.readValue(request.getInputStream(), UserLoginRequestDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Internal server error");
        }

        //2. Login ID, Pass 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getEmail() , loginDto.getPassword());

        //3. User Password 인증이 이루어지는 부분
        //"authenticate" 가 실행될때 "PrincipalDetailService.loadUserByUsername" 실행
        Authentication authenticate =
                //this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
                authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return authenticate;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        this.successHandler.onAuthenticationSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        this.failureHandler.onAuthenticationFailure(request,response, failed);
    }

}
