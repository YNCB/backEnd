package AMS.AMSsideproject.web.apiController;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@Api(tags = "test용 컨트롤러 안보셔도 됩니다!")
public class TestController {

    @GetMapping("/")
    public String home() {
        return "test";
    }

    //test로 직접 인가 코드 받기
    @GetMapping("/login/oauth2/code/kakao")
    @ResponseBody
    public String KakaoCode(@RequestParam("code") String code) {

        return "카카오 로그인 인증완료, code: "  + code;
    }


}
