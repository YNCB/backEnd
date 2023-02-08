package AMS.AMSsideproject.web.auth.jwt.service;

import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import AMS.AMSsideproject.web.exception.JWT.JwtExpireException;
import AMS.AMSsideproject.web.exception.JWT.JwtValidException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 토큰과 관련된 기능
 */
@Configuration //빈으로 등록
@Slf4j
@RequiredArgsConstructor
public class JwtProvider {

    //access token, refresh token, my_session 생성
    public JwtToken createJwtToken(Long user_id, String nickname ,String role) {
        //엑세스 토큰
        String accessToken = JwtProperties.ACCESS_PREPIX_STRING + JWT.create()
                .withSubject(String.valueOf(user_id))
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESSTOKEN_TIME))
                .withClaim("userId", user_id)
                .withClaim("nickName", nickname)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        //리프레시 토큰
        String refreshToken = JWT.create()
                .withSubject(String.valueOf(user_id) + "_refresh")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESHTOKEN_TIME))
                .withClaim("userId", user_id)
//                .withClaim("nickName", nickname)
//                .withClaim("role", role)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return new JwtToken(accessToken, refreshToken);
    }

    //access token 만 생성 -> refresh 토큰 요청이 왔을때
    public String createAccessToken(Long user_id, String nickname, String role) {

        return JwtProperties.ACCESS_PREPIX_STRING + JWT.create()
                .withSubject(String.valueOf(user_id))
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESSTOKEN_TIME))
                .withClaim("userId", user_id)
                .withClaim("nickName", nickname)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    //엑세스 토큰이 헤더에 있는지 검증
    public String validAccessTokenHeader(HttpServletRequest request) {

        String token = request.getHeader(JwtProperties.ACCESS_HEADER_STRING);

        if(!StringUtils.hasText(token)) //토큰이 없는 경우
            return null;
        return token;
    }

    //엑세스 토큰 포멧에서 엑세스 토큰값 파싱하기
    public String parsingAccessToken(String token){

        try{
            //accessToken prefix 찾기
            String replace = token.replace(JwtProperties.ACCESS_PREPIX_STRING, " ");

            //공백 없애기
            String accessToken = replace.trim();
            return accessToken;

        }catch (Exception e){
            throw new JwtValidException("정상적이지 않은 토큰입니다.");
        }
    }

    //리프레시 토큰이 헤더에 있는지 검증
    public String validRefreshTokenHeader(HttpServletRequest request) {

        String token = request.getHeader(JwtProperties.REFRESH_HEADER_STRING);

        if(!StringUtils.hasText(token)) //토큰이 없는 경우
            return null;
        return token;
    }

    public String getNickname(String token){
        return Jwts.parserBuilder().setSigningKey(JwtProperties.SECRET.getBytes()).build().parseClaimsJws(token).getBody().get("nickName").toString();
    }

    public Long getUserId(String token){
        return Long.valueOf(Jwts.parserBuilder().setSigningKey(JwtProperties.SECRET.getBytes()).build().parseClaimsJws(token).getBody().get("userId").toString());
    }

    public String getRole(String token){
        return Jwts.parserBuilder().setSigningKey(JwtProperties.SECRET.getBytes()).build().parseClaimsJws(token).getBody().get("role").toString();
    }

    //JWT 토큰의 만료시간 구하기
    public Long getExpiration(String accessToken){

        Date expiration = Jwts.parserBuilder().setSigningKey(JwtProperties.SECRET.getBytes())
                .build().parseClaimsJws(accessToken).getBody().getExpiration();

        long now = new Date().getTime();
        return expiration.getTime() - now;
    }

    //JWT 토큰 유효성 검사
    public void validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(JwtProperties.SECRET.getBytes())
                    .build().parseClaimsJws(token).getBody();
        }catch (SignatureException e ){
            log.error("SignatureException", e.getMessage());
            throw new JwtValidException(e.getMessage());
        }catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException", e.getMessage());
            throw new JwtExpireException(e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("MalformedJwtException", e.getMessage());
            throw new JwtValidException(e.getMessage());
        }catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException", e.getMessage());
            throw new JwtValidException(e.getMessage());
        }catch (Exception e ){
            log.error("Exception", e.getMessage());
            throw new JwtValidException(e.getMessage());
        }
    }

}
