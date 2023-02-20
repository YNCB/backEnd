package AMS.AMSsideproject.domain.user.service;

import AMS.AMSsideproject.web.jwt.service.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;


@SpringBootTest
class UserServiceTest {

    @Autowired JwtProvider jwtProvider;
    @Autowired UserService userService;
    @Autowired RedisTemplate<String, String> redisTemplate;

//    @Test
//    public void 로그아웃테스트() throws Exception {
//        //given
//        String accessToken = jwtProvider.createAccessToken(1L, "test", "USER");
//
//        //when
//        userService.logout(accessToken);
//
//        //then
//        String blackAccessToken = redisTemplate.opsForValue().get(accessToken);
//        System.out.println(blackAccessToken);
//    }

}