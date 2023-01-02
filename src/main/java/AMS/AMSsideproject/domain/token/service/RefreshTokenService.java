package AMS.AMSsideproject.domain.token.service;

import AMS.AMSsideproject.domain.token.RefreshToken;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.exception.RefreshTokenInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * RefreshToken 토큰과 관련된 기능
 */
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final UserService userService;

    //사용자의 refreshToken 가져오는 메서드
    public Optional<RefreshToken> findRefreshToken(Long userId) {
        User findUser = userService.findUserByUserId(userId);

        return Optional.ofNullable(findUser.getRefreshToken()); //없을수가 있다. -> 처음 회원가입하고 로그인 한 경우

    }

    //refreshToken 저장 메서드
    public RefreshToken saveRefreshToken(Long userId, RefreshToken refreshToken) {
        User findUser = userService.findUserByUserId(userId);
        return findUser.setRefreshToken(refreshToken);
    }

    //refreshToken 업데이트 메서드
    public RefreshToken updateRefreshToken(Long userId, String refreshToken) {
        User findUser = userService.findUserByUserId(userId);
        findUser.getRefreshToken().setRefresh_token(refreshToken);

        return findUser.getRefreshToken();
    }

    //사용자의 refreshToken 과 일치하는지 체크하는 메서드 -> refreshToken 재발급 받을때만 사용(그래서 이것만 트랜잭션 따로 걸어둠)
    @Transactional(readOnly = true)
    public String validRefreshTokenValue(Long userId, String refreshToken) {

        String findRefreshToken = findRefreshToken(userId).get().getRefresh_token();

        //토큰의 값이 잘못된 경우
        if (!findRefreshToken.equals(refreshToken)) {
            throw new RefreshTokenInvalidException("리프레시 토큰이 없거나 잘못된 값입니다.");
        }
        return refreshToken;
    }



}
