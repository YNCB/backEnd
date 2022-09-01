package AMS.AMSsideproject.web.auth.jwt.service;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import AMS.AMSsideproject.web.exception.RefreshTokenExpireException;
import AMS.AMSsideproject.web.exception.RefreshTokenInvalidException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Jwt(access,refresh) 토큰과 관련된 기능
 */
@Service
public class JwtProvider {

    //access token, refresh token 생성
    public JwtToken createJwtToken(Long user_id, String nickname ,String role) {

        //여기다 권한을 넣을야되나?!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 스프링 시큐리티에게 권한 체크를 위임하지 않을거니깐!!!!
        String accessToken = JWT.create()
                .withSubject(String.valueOf(user_id))
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESSTOKEN_TIME))
                .withClaim("userId", user_id)
                .withClaim("nickName", nickname)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        String refreshToken = JWT.create()
                .withSubject(String.valueOf(user_id))
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESHTOKEN_TIME))
                .withClaim("userId", user_id)
                .withClaim("nickName", nickname)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));


        return new JwtToken(accessToken, refreshToken);
    }

    //access token 만 생성 -> refresh 토큰 요청이 왔을때
    public String createAccessToken(Long user_id, String nickname, String role) {

        return JWT.create()
                .withSubject(String.valueOf(user_id))
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESSTOKEN_TIME))
                .withClaim("user_id", user_id)
                .withClaim("nickname", nickname)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

    }

    //사용자의 Token 유효기간 체크 메서드
    public String validTokenExpired(String Token) {

        try { //refreshToken 이 만료되지 않은 경우
            JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(Token).getToken();

        }catch (TokenExpiredException e) { //refreshToken 이 만료된 경우
            throw new RefreshTokenExpireException("리프레시 토큰이 만료되었습니다. 다시 로그인을 해주시기 바랍니다.");
        }

        return Token;
    }

    //JWT 토큰 헤더 검증 기능







}
