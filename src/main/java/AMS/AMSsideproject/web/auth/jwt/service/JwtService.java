package AMS.AMSsideproject.web.auth.jwt.service;

import AMS.AMSsideproject.domain.token.RefreshToken;
import AMS.AMSsideproject.domain.token.service.RefreshTokenService;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import AMS.AMSsideproject.web.exception.RefreshTokenExpireException;
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

    //refresh token 요청시 사용 (access 토큰이 기한 만료인 경우)
    @Transactional
    public JwtToken validRefreshTokenAndRecreateToken(String refresh_token, Long userId) { //nickname????!!!!으로 받아야되나?!

        String accessToken = null;
        String refreshToken = null;
        User findUser = userService.findUserByUserId(userId);

        try { //리프레시 토큰의 기한이 만료되지 않은 경우
            refreshToken = refreshTokenService.validRefreshToken(refresh_token);
            accessToken = jwtProviderService.createAccessToken(findUser.getUser_id(), findUser.getNickname(), findUser.getRole());

        }catch(RefreshTokenExpireException e) { //리프레시 토큰도 기한이 만료된 경우
            JwtToken jwtToken = jwtProviderService.createJwtToken(findUser.getUser_id(), findUser.getNickname(), findUser.getRole());

            refreshTokenService.updateRefreshToken(findUser.getUser_id(), jwtToken.getRefreshToken());
            accessToken = jwtToken.getAccessToken();
            refreshToken = jwtToken.getRefreshToken();
        }

        return new JwtToken(accessToken, refreshToken);
    }


}
