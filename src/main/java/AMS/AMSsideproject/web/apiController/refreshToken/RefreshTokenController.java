package AMS.AMSsideproject.web.apiController.refreshToken;

import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import AMS.AMSsideproject.web.auth.jwt.service.JwtService;
import AMS.AMSsideproject.web.response.DefaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/codebox")
@RequiredArgsConstructor
@Api(tags = "리프레시 토큰 관련 api")
public class RefreshTokenController {

    private final JwtService jwtService;

    /**
     * 여기도 따로 유저 아이디를 주는게 아니니깐!!!!!!! 해당 jwt 유효한지 판별하고나서 해당 토큰에서 아이디 얻어와서 새로운 토큰만들기!!!!!!!!!!!!!!
     */
    @PostMapping("/refreshToken") //인증,권한이 필요한 uri
    @ApiOperation(value = "토큰 재발급 api", notes = "엑세스 토큰이 만료되었을때 리프레시 토큰으로 요청하게 되면 엑세스,리프레시 토큰을 재발급해줍니다. " +
            "리프레시 토큰이 정상적이지 않거나 기한이 만료되었으면 재로그인을 요청합니다.")
    public DefaultResponse recreateToken(@RequestHeader("refreshToken") String refreshToken) {

        //Long userId = (Long) request.getAttribute("userId");

        //엑세스 토큰 재생성
        //엑세스 토큰도 그냥 재생성할까?!!!!!!!!!!!!!!!!! 그럼 jwtService에 "로그인토큰생성" 과 "리프레시 토큰 재생성" 메서드 나눠서 구현!!!!!!!!!!!!!!!!!!!!!!!!!
        //엑세스 토큰만 생성하면 되자나!!!!!!
        //리프레시토큰 검증다 받고 왔자나!!!!
        JwtToken jwtToken = jwtService.recreateTokenUsingToken(refreshToken);

        return new DefaultResponse("200", "토큰이 재발급되었습니다.",jwtToken);
    }

}
