package AMS.AMSsideproject.web.apiController;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.web.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequiredArgsConstructor
@ApiIgnore
public class TestController {

    private final JwtProvider jwtProvider;

    @GetMapping("/test")
    public String home() {
        return "test";
    }

    //test로 직접 인가 코드 받기 - kakao
    @GetMapping("/login/oauth2/code/kakao")
    @ResponseBody
    public String KakaoCode(@RequestParam("code") String code) {

        return "카카오 로그인 인증완료, code: "  + code;
    }

    //test로 직접 인가코드 받기 - google
    @GetMapping("/login/oauth2/code/google")
    @ResponseBody
    public String GoogleCode(@RequestParam("code") String code) {

        return "구글 로그인 인증완료, code " + code;
    }

    @GetMapping("/!jenkinkTest!")
    @ResponseBody
    public String jenkinkTest1() {

        return "!!!!!!!!!!!!!!!!jenkinkTest22!!!!!!!!!!!!!!!!!!!!!!";
    }

    @GetMapping("/tokenParsingTest")
    @ResponseBody
    public String tokenParsingTest(@RequestHeader("Authorization")String token ){

        String accessToken = jwtProvider.parsingAccessToken(token);
        return accessToken;
    }

    @GetMapping("/security-test")
    @ResponseBody
    public String securityTest() {
        User user = SecurityUtil.getCurrentUser();

        System.out.println("id = " + user.getUser_id());
        System.out.println("nickname = " + user.getNickname());
        System.out.println("email = " + user.getEmail());

        return "success";
    }

}
