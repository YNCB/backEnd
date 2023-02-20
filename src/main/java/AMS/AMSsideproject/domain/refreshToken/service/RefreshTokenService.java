package AMS.AMSsideproject.domain.refreshToken.service;

import AMS.AMSsideproject.domain.refreshToken.RefreshToken;
import AMS.AMSsideproject.domain.refreshToken.repository.RefreshTokenRepository;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.repository.UserRepository;
import AMS.AMSsideproject.web.exception.JWT.TokenValidException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
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

    //사용자의 리프레쉬 토큰과 일치하지 검증
    public void validRefreshTokenValue(Long userId, String refreshToken) {

        Optional<RefreshToken> findToken = refreshTokenRepository.find(userId);
        String token = findToken.map(t -> new String(t.getValue())).orElseThrow(() -> new TokenValidException());

        if(!refreshToken.equals(token))
            throw new TokenValidException();
    }
}
