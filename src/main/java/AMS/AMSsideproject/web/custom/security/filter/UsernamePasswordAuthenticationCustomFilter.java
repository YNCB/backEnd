package AMS.AMSsideproject.web.custom.security.filter;

import AMS.AMSsideproject.web.custom.security.filter.form.UserLoginForm;
import AMS.AMSsideproject.web.exception.security.LoginUserInternalException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * "/ams/login" 요청시 로그인 처리를 하는 커스텀 필터
 */
public class UsernamePasswordAuthenticationCustomFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private ObjectMapper objectMapper;
    private UserLoginSuccessCustomHandler successHandler;
    private UserLoginFailureCustomHandler failureHandler;

    public UsernamePasswordAuthenticationCustomFilter(AuthenticationManager authenticationManager , ObjectMapper objectMapper,
                                                      UserLoginSuccessCustomHandler handler ,
                                                      UserLoginFailureCustomHandler failureHandler) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.successHandler = handler;
        this.failureHandler = failureHandler;

    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        //request body 의 정보 받아오기
        UserLoginForm userLoginForm = null;
        try {
            userLoginForm = objectMapper.readValue(request.getInputStream(), UserLoginForm.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userLoginForm.getId() , userLoginForm.getPassword());

        //user password 검사 -> 스프링 시큐리티 세션을 사용하지 않아 저장되지 않는다.(Authentication 객체)
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch (AuthenticationException e ) {
            //회원가입을 하지 않은 사용자 , 비밀번호가 틀린 사용자 그냥 한번에 묶어서 처리
            throw new LoginUserInternalException("아이디 또는 비밀번호를 다시 확인해주시기 바랍니다.");
        }

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
