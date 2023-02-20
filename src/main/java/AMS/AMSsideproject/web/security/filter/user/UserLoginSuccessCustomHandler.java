package AMS.AMSsideproject.web.security.filter.user;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.web.jwt.JwtToken;
import AMS.AMSsideproject.web.jwt.service.JwtService;
import AMS.AMSsideproject.web.security.PrincipalDetails;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.user.UserLoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//"UsernamePasswordCustomFilter" 가 정상적으로 성공할 경우 호출되는 커스텀 Handler => 여기서 JWT 토큰을 반환해준다.
@Slf4j
@Component
@RequiredArgsConstructor
public class UserLoginSuccessCustomHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("로그인 성공");

        //1. 로그인 인증을 마친 사용자 가져오기
        User loginUser  = ((PrincipalDetails) authentication.getPrincipal()).getUser();

        //2. 토큰 생성
        JwtToken jwtToken = jwtService.login(loginUser.getUser_id(), loginUser.getNickname(), loginUser.getRole().name());

        //3. 반환 Dto 생성
        UserLoginDto userLoginDto = new UserLoginDto(loginUser.getUser_id(), loginUser.getNickname(), jwtToken);
        DataResponse dataResponse = new DataResponse(String.valueOf(HttpStatus.OK.value()),
                "로그인을 성공하였습니다. 토큰이 발급되었습니다.", userLoginDto);

        //4. response
        String res = objectMapper.writeValueAsString(dataResponse);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(res);
    }

}
