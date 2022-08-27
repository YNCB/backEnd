package AMS.AMSsideproject.web.auth.jwt.service;

import AMS.AMSsideproject.domain.token.service.RefreshTokenService;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 실제 호출하여 사용하는 jwt 토큰과 관련된 기능을 포함한 서비스
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final RefreshTokenService refreshTokenService;
    private final JwtProviderService jwtProviderService;


    //access, refresh 토큰 생성 및 저장 -> 로그인 한 경우 , 토큰 두개다 만료되었을 경우 두개다 생성해서 저장
    public JwtToken createAndSaveToken() {
        return null;
    }

    //refresh token 요청시 사용
    public JwtToken validRefreshTokenAndRecreateToken() {
        return null;
    }


}
