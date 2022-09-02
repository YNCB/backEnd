package AMS.AMSsideproject.web.custom.security.filter;

import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.custom.security.filter.form.UserLoginForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * "/ams/login" 요청시 로그인 처리를 하는 커스텀 필터
 */
public class UsernamePasswordCustomFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;
    private ObjectMapper objectMapper;

    public UsernamePasswordCustomFilter(AuthenticationManager authenticationManager , JwtProvider jwtProvider, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.objectMapper = objectMapper;
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

        //user password 검사
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);


        return authenticate;
    }
}
