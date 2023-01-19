package AMS.AMSsideproject.web.interceptor;

import AMS.AMSsideproject.domain.refreshToken.service.RefreshTokenService;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.exception.ExpireTokenException;
import AMS.AMSsideproject.web.exception.NotEqRefreshToken;
import AMS.AMSsideproject.web.exception.NotExistingToken;
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
    @Autowired private  ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        BaseErrorResult errorResult = null;

        //토큰이 있는지 판단
        String refreshToken = jwtProvider.validRefreshTokenHeader(request);

        try{
            //header 에서 JWT 토큰이 있는지 검사
            if(!StringUtils.hasText(refreshToken))  //토큰이 없는 경우
                throw new NotExistingToken("토큰이 없습니다.");

            //토큰 유효기간 검증
            jwtProvider.validTokenExpired(refreshToken);

            //토큰에서 userId 추출
            Long findUserId = jwtProvider.getUserIdToToken(refreshToken);

            //요청한 사용자의 refreshToken 과 일치하는지 검증
            refreshTokenService.validRefreshTokenValue(findUserId, refreshToken);

            return true; //정상적인 경우

        }catch (NotExistingToken e){ //헤더에 토큰이 없는경우-412
            sendResponse(response, e.getMessage(),
                    HttpStatus.PRECONDITION_FAILED.value(),HttpStatus.PRECONDITION_FAILED.getReasonPhrase() );
            return false;
        }catch (ExpireTokenException e){ //토큰 유효기간이 만료-401
            sendResponse(response, e.getMessage(),
                    HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return false;
        }catch (NotEqRefreshToken e) { //자신의 토큰이 아님-401
            sendResponse(response, e.getMessage(),
                    HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
            return false;
        }
        catch (Exception e) { //서버 에러
            sendResponse(response, e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return false;
        }
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
