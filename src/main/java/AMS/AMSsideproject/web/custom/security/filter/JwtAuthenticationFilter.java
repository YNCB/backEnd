package AMS.AMSsideproject.web.custom.security.filter;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.custom.security.PrincipalDetails;
import AMS.AMSsideproject.web.exception.JWT.JwtExpireException;
import AMS.AMSsideproject.web.exception.JWT.JwtExistingException;
import AMS.AMSsideproject.web.exception.JWT.JwtValidException;
import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.RedisTemplate;
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
    private RedisTemplate<String,String> redisTemplate;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider, ObjectMapper objectMapper,
                                   UserService userService, RedisTemplate<String,String> redisTemplate) {
        super(authenticationManager);
        this.jwtProvider = jwtProvider;
        this.objectMapper = objectMapper;
        this.userService = userService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String token = jwtProvider.validAccessTokenHeader(request);

        try {
            //header 에서 JWT 토큰이 있는지 검사
            if(!StringUtils.hasText(token))  //토큰이 없는 경우
                throw new JwtExistingException("토큰이 없습니다.");

            //token parsing
            String accessToken = jwtProvider.parsingAccessToken(token);

            //로그아웃된 토큰인지 검사
            validBlackToken(accessToken);

            //토큰 유효성 검사
            jwtProvider.validateToken(accessToken);

            /**
             * 권한을 그냥 "USER" 체크 할까?? -> 그럼 spring security context 안에 넣어서 스프링 시큐리티에게 권한처리 위임하면 되는데....
             * 이게 좋은 방식인가?!!!!???????
             * 이방식이에서 궁금한점이 uri 요청올 때 spring security context에 Authentication 객체 저장했다가 모든 응답이 끝나면 해당 객체는 사라지나?!!!!!!!!!!!!!
             * -> 그러면 괜찮은거지!!!!!!! (지속적인 세션을 사용하지 않는거고 권한처리도 쉬우니!!!!!!!!!!!!!!!!!!!!!!!)
             */
            Long userId = jwtProvider.getUserId(accessToken);
            User findUser = userService.findUserByUserId(userId);
            PrincipalDetails principalDetails = new PrincipalDetails(findUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                   principalDetails, // 나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                   null, // 패스워드는 모르니까 null 처리, 어차피 지금 로그인 인증하는게 아니니까!!(로그인 필터를 사용하는게 아니니깐 지금)
                   principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication); //추가로 controller 단에서 해당 객체를 꺼낼수 있다.!

            chain.doFilter(request,response);

        }catch (JwtExpireException e) { //기한만료된 토큰-401
            sendResponse(response, "토큰의 기한이 만료되었습니다.",
                    HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }catch (JwtExistingException e){ //헤더에 토큰이 없는경우-412
            sendResponse(response, e.getMessage(),
                    HttpStatus.PRECONDITION_FAILED.value(),HttpStatus.PRECONDITION_FAILED.getReasonPhrase() );
            return;
        }catch (JwtValidException e) { //정상적이지 않은 토큰-401
            sendResponse(response, "정상적이지 않은 토큰입니다.",
                    HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return;
        }
        catch (Exception e) { //나머지 서버 에러-500
            sendResponse(response, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return;
        }
    }

    private void validBlackToken(String accessToken) {

        //Redis에 있는 엑세스 토큰인 경우 로그아웃 처리된 엑세스 토큰임.
        String blackToken = redisTemplate.opsForValue().get(accessToken);
        if(StringUtils.hasText(blackToken))
            throw new JwtValidException("로그아웃 처리된 엑세스 토큰입니다.");
    }

    private void sendResponse(HttpServletResponse response, String message, int code, String status ) throws IOException {

        BaseErrorResult result = new BaseErrorResult(message, String.valueOf(code), status);

        String res = objectMapper.writeValueAsString(result);
        response.setStatus(code);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(res);
    }
}
