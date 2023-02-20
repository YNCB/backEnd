package AMS.AMSsideproject.web.jwt.service;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.repository.UserRepository;
import AMS.AMSsideproject.web.jwt.JwtProperties;
import AMS.AMSsideproject.web.jwt.JwtToken;
import AMS.AMSsideproject.web.exception.JWT.TokenExpireException;
import AMS.AMSsideproject.web.exception.JWT.TokenValidException;
import AMS.AMSsideproject.web.security.PrincipalDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.Optional;

/**
 * 토큰과 관련된 기능
 */
@Slf4j
@Component //빈으로 등록
public class JwtProvider {

    public static final long ACCESSTOKEN_TIME = 1000 * 60 * 30;            // 30분
    public static final long REFRESHTOKEN_TIME = 1000 * 60 * 60 * 24 * 7;  //7일
    public static final String ACCESS_PREFIX_STRING = "Bearer ";
    public static final String ACCESS_HEADER_STRING = "Authorization";
    public static final String REFRESH_HEADER_STRING = "RefreshToken";
    private final Key key;

    private final RedisTemplate<String,String> redisTemplate;
    private final UserRepository userRepository;

    public JwtProvider(@Value("${jwt.secret}")String secret, UserRepository userRepository, RedisTemplate<String,String> redisTemplate) {

        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    //토큰 생성
    public JwtToken createJwtToken(Long userId, String nickname ,String role) {

        //엑세스 토큰
        String accessToken = ACCESS_PREFIX_STRING + Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("userId",userId)
                .claim("nickName", nickname)
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESSTOKEN_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        //리프레시 토큰
        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(userId) + "_refresh")
                .claim("userId", userId)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESHTOKEN_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        return new JwtToken(accessToken, refreshToken);
    }

    //access token 만 생성 -> refresh 토큰 요청이 왔을때
    public String createAccessToken(Long user_id, String nickname, String role) {

        return  ACCESS_PREFIX_STRING + Jwts.builder()
                .setSubject(String.valueOf(user_id))
                .claim("userId",user_id)
                .claim("nickName", nickname)
                .claim("role", role)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESSTOKEN_TIME))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    //엑세스 토큰에서 인증 정보 객체 생성
    public Authentication getAuthentication(String token){

        //이전에 토큰 검증은 끝냈으니 Claims 를 받아와도 에러가 발생하지 않는다.
        Claims claims = parseClaims(token);

        //1. 토큰안에 필요한 Claims 가 있는지 확인
        if(claims.get("userId")==null && claims.get("nickName")==null && claims.get("role")==null)
            return null;

        //2. DB 에 사용자가 있는지 확인 -> 탈퇴했을 경우를 위해서
        Long userId = Long.valueOf(claims.get("userId").toString());
        Optional<User> findUser = userRepository.findByUserId(userId);
        if(findUser.isEmpty())
            return null;

        UserDetails userDetails = new PrincipalDetails(findUser.get());
        //추후에 권한 검사를 하기 때문에 credentials 는 굳이 필요없음
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    //토큰 유효성 검사
    public Boolean validationToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key)
                    .build().parseClaimsJws(token).getBody();
        }catch (SignatureException e ){
            log.error("SignatureException", e.getMessage());
            return false;
        }catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException", e.getMessage());
            return false;
        } catch (MalformedJwtException e) {
            log.error("MalformedJwtException", e.getMessage());
            return false;
        }catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException", e.getMessage());
            return false;
        }catch (Exception e ){
            log.error("Exception", e.getMessage());
            return false;
        }
        return true;
    }

    //엑세스 토큰의 만료시간
    public Long getExpiration(String accessToken){

        Date expiration = Jwts.parserBuilder().setSigningKey(key)
                .build().parseClaimsJws(accessToken).getBody().getExpiration();

        long now = new Date().getTime();
        return expiration.getTime() - now;
    }

    //로그아웃된 엑세스 토큰인지 검증
    public Boolean validBlackToken(String accessToken) {

        //Redis 에 있는 엑세스 토큰인 경우 로그아웃 처리된 엑세스 토큰임.
        String blackToken = redisTemplate.opsForValue().get(accessToken);
        if(StringUtils.hasText(blackToken))
            return false;
        return true;
    }

    //토큰 Claims 가져오기
    public Claims parseClaims(String accessToken){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
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
            String replace = token.replace(ACCESS_PREFIX_STRING, " ");

            //공백 없애기
            String accessToken = replace.trim();
            return accessToken;
        }catch (Exception e){
            throw new TokenValidException("정상적이지 않은 토큰입니다.");
        }
    }

    public String getNickname(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("nickName").toString();
    }

    public Long getUserId(String token){
        return Long.valueOf(Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("userId").toString());
    }

    public String getRole(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role").toString();
    }

    //JWT 토큰 유효성 검사
    public void validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key)
                    .build().parseClaimsJws(token).getBody();
        }catch (SignatureException e ){
            log.error("SignatureException", e.getMessage());
            throw new TokenValidException(e.getMessage());
        }catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException", e.getMessage());
            throw new TokenExpireException(e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("MalformedJwtException", e.getMessage());
            throw new TokenValidException(e.getMessage());
        }catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException", e.getMessage());
            throw new TokenValidException(e.getMessage());
        }catch (Exception e ){
            log.error("Exception", e.getMessage());
            throw new TokenValidException(e.getMessage());
        }
    }

}
