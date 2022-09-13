package AMS.AMSsideproject.web.auth.jwt.service;

import AMS.AMSsideproject.domain.token.RefreshToken;
import AMS.AMSsideproject.domain.token.service.RefreshTokenService;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserJoinForm;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@SpringBootTest
class JwtServiceTest {

    @Autowired JwtService jwtService;
    @Autowired UserService userService;
    @Autowired RefreshTokenService refreshTokenService;

    @BeforeEach
    @Transactional
    public void init() {
        UserJoinForm userJoinForm = new UserJoinForm("test1","test1", "test1", 1999, 07,27, "test1", "test1", "test1", "test1");
        userService.join(userJoinForm);
    }

    @Test
    @Transactional
    //@Rollback(value = false)
    public void 회원가입후첫로그인일경우의JWT토큰생성테스트() throws Exception {
        //given
        User findUser = userService.findUserByNickName("test1");

        //when
        JwtToken jwtToken = jwtService.createAndSaveToken(findUser.getUser_id(), findUser.getNickname(), findUser.getRole());

        //then
        String nickName = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(jwtToken.getAccessToken()).getClaim("nickName").asString();
        Assertions.assertThat(nickName).isEqualTo("test1");

        Optional<RefreshToken> refreshToken = refreshTokenService.findRefreshToken(findUser.getUser_id());
        Assertions.assertThat(jwtToken.getRefreshToken()).isEqualTo(refreshToken.get().getRefresh_token());
    }
    
    @Test
    @Transactional
    public void 재로그인인경우의JWT토큰생성테스트() throws Exception {

        //given
        User findUser = userService.findUserByNickName("test1");
        JwtToken firstToken = jwtService.createAndSaveToken(findUser.getUser_id(), findUser.getNickname(), findUser.getRole());//처음 로그인시 얻은 accessToken
        Date firstExpire = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(firstToken.getRefreshToken()).getExpiresAt();//처음 로그인시 저장한 refreshToken

        Thread.sleep(2000);

        //when
        JwtToken secondToken = jwtService.createAndSaveToken(findUser.getUser_id(), findUser.getNickname(), findUser.getRole());//두번째 로그인
        Date secondExpire = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(secondToken.getRefreshToken()).getExpiresAt();

        //then
        Assertions.assertThat(firstExpire).isNotEqualTo(secondExpire);
    }

    @Test
    @Transactional
    public void 만료되지않은리프레시토큰요청시에검증테스트() throws Exception {

        //given - 첫번쨰 로그인
        User findUser = userService.findUserByNickName("test1");

        JwtToken jwtToken1 = jwtService.createAndSaveToken(findUser.getUser_id(), findUser.getNickname(), findUser.getRole());

        //when - 두번째 로그인
        //JwtToken jwtToken2 = jwtService.validRefreshToken(findUser.getRefreshToken().getRefresh_token(), findUser.getUser_id());

        //then - 리프레시토큰은 재발급 x , 엑세스토큰 발급
        //Assertions.assertThat(jwtToken1.getAccessToken()).isNotEqualTo(jwtToken2.getAccessToken());
    }


}