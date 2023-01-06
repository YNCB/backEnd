package AMS.AMSsideproject.web.custom.security.filter;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.custom.security.PrincipalDetails;
import AMS.AMSsideproject.web.exception.JWTTokenExpireException;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 기본 인증 처리 -> 기본적으로 인증이 필요한 uri 일경우에 header 에서 JWT 토큰 검증
 */
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private JwtProvider jwtProvider;
    private ObjectMapper objectMapper;
    private UserService userService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider, ObjectMapper objectMapper, UserService userService) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        this.objectMapper = objectMapper;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = jwtProvider.validAccessTokenHeader(request);
        String message = null;

        try {
            //header 에서 JWT 토큰이 있는지 검사
            if(!StringUtils.hasText(token))  //토큰이 없는 경우
                throw new Exception();

            //JWT 토큰 만료기간 검증
            jwtProvider.validTokenExpired(token);

            /**
             * refreshToken이 탈취당하였을때 refreshToken을 accessToken인척 사용할수 있기 때문에 구분해주기 위해서!!!
             */
            if(!jwtProvider.validTokenHeaderUser(token))
                throw new Exception();


            /**
             * 권한을 그냥 "USER" 체크 할까?? -> 그럼 spring security context 안에 넣어서 스프링 시큐리티에게 권한처리 위임하면 되는데....
             * 이게 좋은 방식인가?!!!!???????
             * 이방식이에서 궁금한점이 uri 요청올 때 spring security context에 Authentication 객체 저장했다가 모든 응답이 끝나면 해당 객체는 사라지나?!!!!!!!!!!!!!
             * -> 그러면 괜찮은거지!!!!!!! (지속적인 세션을 사용하지 않는거고 권한처리도 쉬우니!!!!!!!!!!!!!!!!!!!!!!!)
             */
            Long userId = jwtProvider.getUserIdToToken(token);
            User findUser = userService.findUserByUserId(userId);
            PrincipalDetails principalDetails = new PrincipalDetails(findUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                   principalDetails, // 나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                   null, // 패스워드는 모르니까 null 처리, 어차피 지금 로그인 인증하는게 아니니까!!(로그인 필터를 사용하는게 아니니깐 지금)
                   principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication); //추가로 controller 단에서 해당 객체를 꺼낼수 있다.!

            chain.doFilter(request,response);

        }catch (JWTTokenExpireException e) {
            message = "엑세스 토큰의 유효기간이 만료되었습니다. 리프레쉬 토큰을 요청해주십시오.";
            sendRefreshResponse(message, response);
            return;
        }catch (Exception e ) {
            message = "토큰이 없거나 정상적인 값이 아닙니다.";
            sendErrorResponse(message, response);
            return;
        }
    }

    private void sendErrorResponse(String message, HttpServletResponse response) throws IOException {
        BaseErrorResult errorResult = new BaseErrorResult(message,"401", "Unauthorized");
        String res = objectMapper.writeValueAsString(errorResult);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }

    private void sendRefreshResponse(String message, HttpServletResponse response) throws IOException {
        BaseResponse baseResponse = new BaseResponse("201", message);
        String res = objectMapper.writeValueAsString(baseResponse);

        response.setStatus(HttpStatus.CREATED.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }

}
