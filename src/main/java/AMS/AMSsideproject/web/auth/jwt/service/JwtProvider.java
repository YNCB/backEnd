package AMS.AMSsideproject.web.auth.jwt.service;

import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import AMS.AMSsideproject.web.exception.user.JWTTokenExpireException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Jwt(access,refresh) 토큰과 관련된 기능
 */
@Configuration //빈으로 등록
@RequiredArgsConstructor
public class JwtProvider {

    //access token, refresh token, my_session 생성
    public JwtToken createJwtToken(Long user_id, String nickname ,String role) {
        //엑세스 토큰
        String accessToken = JWT.create()
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

        //마이페이지 토큰 -> 나의 페이지에 접속할때 사용되는 토큰!!!!!!!!(나의 페이지와 다른 유저 페이지가 기능이 달라 구분되어야 되기때문에)
        String my_session = JWT.create()
                .withSubject(String.valueOf(user_id) + "_mySession")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESSTOKEN_TIME))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        return new JwtToken(accessToken, refreshToken, my_session);
    }

    //access token 만 생성 -> refresh 토큰 요청이 왔을때
    public String createAccessToken(Long user_id, String nickname, String role) {

        return JWT.create()
                .withSubject(String.valueOf(user_id))
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESSTOKEN_TIME))
                .withClaim("userId", user_id)
                .withClaim("nickName", nickname)
                .withClaim("role", role)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }
    //my_session token 만 생성 -> refresh 토큰 요청이 왔을때
    public String createMySessionToken(Long user_id) {
        return JWT.create()
                .withSubject(String.valueOf(user_id) + "_mySession")
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESSTOKEN_TIME))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }


    //사용자 Token 유효기간 검증
    public String validTokenExpired(String Token) {

        try { //refreshToken 이 만료되지 않은 경우
            JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(Token).getToken();

        }catch (TokenExpiredException e) { //Token 이 만료된 경우
            throw new JWTTokenExpireException("토큰이 만료되었습니다.");
        }

        return Token;
    }

    //JWT 엑세스 토큰이 헤더에 있는지 검증
    public String validAccessTokenHeader(HttpServletRequest request) {

        String token = request.getHeader(JwtProperties.ACCESS_HEADER_STRING);

        if(!StringUtils.hasText(token)) //토큰이 없는 경우
            return null;
        return token;
    }

    //JWT 리프레시 토큰이 헤더에 있는지 검증
    public String validRefreshTokenHeader(HttpServletRequest request) {

        String token = request.getHeader(JwtProperties.REFRESH_HEADER_STRING);

        if(!StringUtils.hasText(token)) //토큰이 없는 경우
            return null;
        return token;
    }

    //JWT 토큰의 key 값들이 정상적인 검증
    public Boolean validTokenHeaderUser(String token) {

        List<String> keys = new ArrayList<>();
        keys.add("userId"); keys.add("nickName"); keys.add("role"); //keys.add("sub"); keys.add("exp");

        Set<String> strings = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token).getClaims().keySet();
        boolean check = strings.containsAll(keys);

        return check;
    }

    //JWT 토큰에서 user nickId 가져오는 기능
    public Long getUserIdToToken(String token) {
        Long userId = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token).getClaim("userId").asLong();
        return userId;
    }
    //JWT 토큰에서 user nickname 가져오는 기능
    public String getNickNameToToken(String token) {
        String nickName= JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token).getClaim("nickName").asString();
        return nickName;
    }
    //JWT 토큰에서 user role 가져오는 기능
    public String getRoleToToken(String token) {
        String role = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token).getClaim("role").asString();
        return role;
    }

}
