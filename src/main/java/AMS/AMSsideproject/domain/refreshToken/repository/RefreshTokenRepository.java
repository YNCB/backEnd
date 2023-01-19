package AMS.AMSsideproject.domain.refreshToken.repository;

import AMS.AMSsideproject.domain.refreshToken.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    //사용자 리프레쉬 토큰 조회
    public Optional<RefreshToken> find(Long userId);

    //리프레시 토큰 저장
    public void save(RefreshToken refreshToken);

}
