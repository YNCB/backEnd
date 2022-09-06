package AMS.AMSsideproject.web.custom.security.filter;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import AMS.AMSsideproject.web.auth.jwt.service.JwtService;
import AMS.AMSsideproject.web.custom.security.PrincipalDetails;
import AMS.AMSsideproject.web.dto.user.UserLoginDto;
import AMS.AMSsideproject.web.response.defaultResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <일반 로그인 ></일반>
 * "UsernamePasswordCustomFilter" 가 정상적으로 성공할 경우 호출되는 커스텀 Handler
 * => 여기서 JWT 토큰을 반환해준다.
 *
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * 만약 회원가입하지 않은 사용자이면 "실패 Handler" 에서 처리?!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *
 */
@Component
@RequiredArgsConstructor
public class UserLoginSuccessCustomHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //로그인 인증을 마친 사용자 가져오기
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        User LoginUser = principal.getUser();

        //토큰 생성
        JwtToken jwtToken = jwtService.createAndSaveToken(LoginUser.getUser_id(), LoginUser.getNickname(), LoginUser.getRole());

        //반환 json 객체 생성
        UserLoginDto userLoginDto = UserLoginDto.builder()
                .user_Id(LoginUser.getUser_id())
                .nickname(LoginUser.getNickname())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();

        defaultResponse defaultResponse = new defaultResponse("200", "로그인을 성공하였습니다. 토큰이 발급되었습니다.", userLoginDto);
        String res = objectMapper.writeValueAsString(defaultResponse);

        //response 에 json 객체 담기
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(res);
    }

}
