package AMS.AMSsideproject.web.auth.jwt.service;

import AMS.AMSsideproject.domain.token.RefreshToken;
import AMS.AMSsideproject.domain.token.service.RefreshTokenService;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
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
    private final JwtProvider jwtProvider;
    private final UserService userService;

    //access, refresh 토큰 생성 및 저장 -> 로그인 한 경우(처음이용자 or 기존 이용자)
    @Transactional
    public JwtToken createAndSaveToken(Long userId, String nickName, String role) {

        //access, refresh 토큰 생성
        JwtToken createToken = jwtProvider.createJwtToken(userId, nickName, role);

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

    //refreshToken 검증 기능
    public JwtToken validRefreshToken(Long user_id, String refreshToken) {

        User findUser = userService.findUserByUserId(user_id);

        //토큰의 값이 정상적인지 판별
        refreshTokenService.validRefreshTokenValue(user_id, refreshToken);

        //토큰의 만료기간이 유효한지 판별
        jwtProvider.validTokenExpired(refreshToken);

        //정상적인 경우
        String accessToken = jwtProvider.createAccessToken(findUser.getUser_id(), findUser.getNickname(), findUser.getRole());
        return new JwtToken(accessToken, refreshToken);
    }

}

