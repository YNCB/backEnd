package AMS.AMSsideproject.domain.token.service;

import AMS.AMSsideproject.domain.token.RefreshToken;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.exception.RefreshTokenValidException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RefreshTokenService {

    private final UserService userService;

    //사용자의 refreshToken 가져오는 메서드
    public RefreshToken findRefreshToken(Long userId) {
        User findUser = userService.findUserByUserId(userId);
        RefreshToken refreshToken = findUser.getRefreshToken();

        return refreshToken; //없을수가 없다!!!!!!!!!!!한번이라도 로그인하였으면!!!!!!!!!
    }

    //사용자의 refreshToken 유효기간 체크 메서드
    public String validRefreshToken(String refreshToken) {

        DecodedJWT verify = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(refreshToken);

        //refreshToken 만료된 경우
        if(verify.getExpiresAt().before(new Date())) {
            throw new RefreshTokenValidException("리프레시 토큰이 만료되었습니다");
        }

        //refreshToken 이 만료되지 않은 경우
        return refreshToken;
    }

    //refreshToken 저장 메서드
    @Transactional
    public RefreshToken saveRefreshToken(Long userId, String refreshToken) {
        User findUser = userService.findUserByUserId(userId);
        return findUser.SetRefreshToken(refreshToken);
    }


}
