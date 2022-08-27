package AMS.AMSsideproject.web.auth.jwt.service;

import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Jwt(access,refresh) 토큰 생성과 관련된 서비스
 */
@Service
public class JwtProviderService {

    //access token, refresh token 생성
    public JwtToken createJwtToken(Long user_id, String nickname ,String role) {

        //여기다 권한을 넣을야되나?!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! 스프링 시큐리티에게 권한 체크를 위임하지 않을거니깐!!!!
        String accessToken = JWT.create()
                .withSubject(String.valueOf(user_id))
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESSTOKEN_TIME))
                .withClaim("user_id", user_id)
                .withClaim("nickname", nickname)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        String refreshToken = JWT.create()
                .withSubject(String.valueOf(user_id))
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESHTOKEN_TIME))
                .withClaim("user_id", user_id)
                .withClaim("nickname", nickname)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));


        return new JwtToken(accessToken, refreshToken);
    }

    //access token 만 생성
    public String createAccessToken(Long user_id, String nickname, String role) {

        return JWT.create()
                .withSubject(String.valueOf(user_id))
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESSTOKEN_TIME))
                .withClaim("user_id", user_id)
                .withClaim("nickname", nickname)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

    }


}
