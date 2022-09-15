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
    public JwtToken createAndSaveTokenByLogin(Long userId, String nickName, String role) {

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

    //refreshToken 의정보를 가지고 AccessToken을 생성하는 기능 -> refreshToken 재발급 받을때만 사용
    @Transactional
    public JwtToken recreateTokenUsingTokenInfo(String token) {

        Long userId = jwtProvider.getUserIdToToken(token);
        String nickName = jwtProvider.getNickNameToToken(token);
        String role = jwtProvider.getRoleToToken(token);

        String accessToken = jwtProvider.createAccessToken(userId, nickName, role);

        return new JwtToken(accessToken, token);
    }

    //Token 에서 user 찾는 메서드
    public User findUserToToken(String token) {

        Long findUserId = jwtProvider.getUserIdToToken(token);
        return userService.findUserByUserId(findUserId);
    }


}

