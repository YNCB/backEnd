package AMS.AMSsideproject.web.auth.jwt.service;

import AMS.AMSsideproject.domain.token.RefreshToken;
import AMS.AMSsideproject.domain.token.service.RefreshTokenService;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import AMS.AMSsideproject.web.exception.RefreshTokenExpireException;
import AMS.AMSsideproject.web.exception.RefreshTokenInvalidException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 실제 호출하여 사용하는 jwt 토큰과 관련된 기능을 포함한 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JwtService {

    private final RefreshTokenService refreshTokenService;
    private final JwtProviderService jwtProviderService;
    private final UserService userService;

    //access, refresh 토큰 생성 및 저장 -> 로그인 한 경우(처음이용자 or 기존 이용자)
    @Transactional
    public JwtToken createAndSaveToken(Long userId, String nickName, String role) {

        //access, refresh 토큰 생성
        JwtToken createToken = jwtProviderService.createJwtToken(userId, nickName, role);

        //리프레시 토큰이 있는 사용자인지 없는 사용자인지 판별
        Optional<RefreshToken> findRefreshToken = refreshTokenService.findRefreshToken(userId);

        if(findRefreshToken.isEmpty()) { //회원가입하고 처음 로그인 하는 사용자
            RefreshToken refreshToken = new RefreshToken(createToken.getRefreshToken());
            refreshTokenService.saveRefreshToken(userId, refreshToken);

        } else { //기존 이용자
            refreshTokenService.updateRefreshToken(userId, createToken.getRefreshToken());
        }

        return createToken;
    }

    //실제 refreshToken 검증 메서드
    public JwtToken validRefreshToken(Long user_id, String refreshToken) {

        User findUser = userService.findUserByUserId(user_id);

        //토큰의 값이 정상적인지 판별
        validRefreshTokenValue(findUser, refreshToken);

        //토큰의 만료기간이 유호한지 판별
        validRefreshTokenExpired(refreshToken);

        //정상적인 경우
        String accessToken = jwtProviderService.createAccessToken(findUser.getUser_id(), findUser.getNickname(), findUser.getRole());
        return new JwtToken(accessToken, refreshToken);
    }

    //사용자의 refreshToken 유효기간 체크 메서드
    public String validRefreshTokenExpired(String refreshToken) {

        String validToken = null;
        try { //refreshToken 이 만료되지 않은 경우
            validToken = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(refreshToken).getToken();

        }catch (TokenExpiredException e) { //refreshToken 이 만료된 경우
            throw new RefreshTokenExpireException("리프레시 토큰이 만료되었습니다. 다시 로그인을 해주시기 바랍니다.");
        }

        return validToken;
    }

    //사용자의 refreshToken 과 일치하는지 체크하는 메서드
    public String validRefreshTokenValue(User user, String refreshToken) {

        String userRefreshToken = user.getRefreshToken().getRefresh_token();

        //토큰의 값이 잘못된 경우
        if (!userRefreshToken.equals(refreshToken)) {
            throw new RefreshTokenInvalidException("리프레시 토큰이 없거나 잘못된 값입니다.");
        }
        return refreshToken;
    }

}
