package AMS.AMSsideproject.web.jwt.service;

import AMS.AMSsideproject.domain.refreshToken.RefreshToken;
import AMS.AMSsideproject.domain.refreshToken.service.RefreshTokenService;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.jwt.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * jwt 토큰과 관련된 기능을 포함한 서비스
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;
    private final UserService userService;

    //토큰 생성 및 저장
    public JwtToken login(Long userId, String nickName, String role) {

        //토큰 생성
        JwtToken createToken = jwtProvider.createJwtToken(userId, nickName, role);

        //리프레시 토큰이 있는 사용자인지 없는 사용자인지 판별
        Optional<RefreshToken> findRefreshToken = refreshTokenService.findRefreshToken(userId);

        if(findRefreshToken.isEmpty()) { //회원가입하고 처음 로그인 하는 사용자, 로그아웃한 사용자
            RefreshToken refreshToken = new RefreshToken(createToken.getRefreshToken());
            refreshTokenService.saveRefreshToken(userId, refreshToken);

        } else { //기존 이용자(회원수정을 한 사용자인 경우)
            refreshTokenService.updateRefreshToken(userId, createToken.getRefreshToken());
        }

        return createToken;
    }

    //리프레쉬토큰을 통해 accessToken, refreshToken 재발급
    public JwtToken reissue(String token) {

        //리프레쉬 토큰에서 userId 추출
        Long userId = jwtProvider.getUserId(token);

        //User 검색
        User findUser = userService.findUserByUserId(userId);

        //엑세스 토큰 재생성
        String accessToken = jwtProvider.createAccessToken(userId, findUser.getNickname(), findUser.getRole().name());

        return new JwtToken(accessToken, token);
    }















    //Token 에서 user 찾는 메서드 -> 회원 수정할때 사용
    @Transactional(readOnly = true)
    public User findUserToToken(String token) {

        Long findUserId = jwtProvider.getUserId(token);

        return userService.findUserByUserId(findUserId);
    }

}

