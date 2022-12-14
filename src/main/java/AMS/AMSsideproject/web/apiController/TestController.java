package AMS.AMSsideproject.web.apiController;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Controller
@RequiredArgsConstructor
@ApiIgnore
public class TestController {

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

}
