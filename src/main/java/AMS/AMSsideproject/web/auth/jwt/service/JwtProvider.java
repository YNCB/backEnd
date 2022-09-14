package AMS.AMSsideproject.web.auth.jwt.service;

import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import AMS.AMSsideproject.web.exception.JWTTokenExpireException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Jwt(access,refresh) 토큰과 관련된 기능
 */
@Configuration
@RequiredArgsConstructor
public class JwtProvider {

    private final UserService userService;

    //access token, refresh token 생성
    @Bean
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
    @Bean
    public String createAccessToken(Long user_id, String nickname, String role) {

        return JWT.create()
                .withSubject(String.valueOf(user_id))
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESSTOKEN_TIME))
                .withClaim("userId", user_id)
                .withClaim("nickName", nickname)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

    }


    //사용자 Token 유효기간 검증
    @Bean
    public String validTokenExpired(String Token) {

        try { //refreshToken 이 만료되지 않은 경우
            JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(Token).getToken();

        }catch (TokenExpiredException e) { //Token 이 만료된 경우
            throw new JWTTokenExpireException("토큰이 만료되었습니다.");
        }

        return Token;
    }

    //JWT 엑세스 토큰이 헤더에 있는지 검증
    @Bean
    public String validAccessTokenHeader(HttpServletRequest request) {

        String token = request.getHeader(JwtProperties.HEADER_STRING);

        if(!StringUtils.hasText(token)) //토큰이 없는 경우
            return null;
        return token;
    }

    //JWT 리프레시 토큰이 헤더에 있는지 검증
    @Bean
    public String validRefreshTokenHeader(HttpServletRequest request) {

        String token = request.getHeader(JwtProperties.REFRESH_HEADER_STRING);

        if(!StringUtils.hasText(token)) //토큰이 없는 경우
            return null;
        return token;
    }

    //JWT 토큰의 key 값들이 정상적인 검증
    @Bean
    public Boolean validTokenHeaderUser(String token) {

        List<String> keys = new ArrayList<>();
        keys.add("userId"); keys.add("nickName"); keys.add("role"); //keys.add("sub"); keys.add("exp");

        Set<String> strings = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token).getClaims().keySet();
        boolean check = strings.containsAll(keys);

        return check;
    }

    //JWT 토큰에서 user nickId 가져오는 기능
    @Bean
    public Long getUserIdToToken(String token) {
        Long userId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token).getClaim("userId").asLong();
        return userId;
    }
    //JWT 토큰에서 user nickname 가져오는 기능
    @Bean
    public String getNickNameToToken(String token) {
        String nickName= JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token).getClaim("nickName").asString();
        return nickName;
    }
    //JWT 토큰에서 user role 가져오는 기능
    @Bean
    public String getRoleToToken(String token) {
        String role = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token).getClaim("role").asString();
        return role;
    }

}
