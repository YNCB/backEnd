package AMS.AMSsideproject.domain.refreshToken.service;

import AMS.AMSsideproject.domain.refreshToken.RefreshToken;
import AMS.AMSsideproject.domain.refreshToken.repository.RefreshTokenRepository;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.repository.UserRepository;
import AMS.AMSsideproject.web.exception.NotEqRefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * RefreshToken 토큰과 관련된 기능
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    //사용자의 refreshToken 가져오는 메서드
    public Optional<RefreshToken> findRefreshToken(Long userId) {
        return refreshTokenRepository.find(userId);
    }

    //refreshToken 저장 메서드
    @Transactional
    public RefreshToken saveRefreshToken(Long userId, RefreshToken refreshToken) {
        Optional<User> findUser = userRepository.findByUserId(userId);
        refreshToken.setUser(findUser.get());
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    //refreshToken 업데이트 메서드
    @Transactional
    public RefreshToken updateRefreshToken(Long userId, String refreshToken) {
        Optional<RefreshToken> findToken = refreshTokenRepository.find(userId);
        findToken.get().setRefreshToken(refreshToken);

        return findToken.get();
    }

    //사용자의 refreshToken 과 일치하는지 체크하는 메서드 -> refreshToken 재발급 받을때만 사용(그래서 이것만 트랜잭션 따로 걸어둠)
    public String validRefreshTokenValue(Long userId, String refreshToken) {

        String findRefreshToken = findRefreshToken(userId).get().getValue();

        //토큰의 값이 잘못된 경우
        if (!findRefreshToken.equals(refreshToken)) {
            throw new NotEqRefreshToken("리프레시 토큰이 잘못된 값입니다.");
        }
        return refreshToken;
    }

}
