package AMS.AMSsideproject.web.apiController.refreshToken;

import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import AMS.AMSsideproject.web.auth.jwt.service.JwtService;
import AMS.AMSsideproject.web.response.DefaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/codebox")
@RequiredArgsConstructor
@Api(tags = "리프레시 토큰 관련 api")
public class RefreshTokenController {

    private final JwtService jwtService;
    private final UserService userService;


    /**
     * 여기도 따로 유저 아이디를 주는게 아니니깐!!!!!!! 해당 jwt 유효한지 판별하고나서 해당 토큰에서 아이디 얻어와서 새로운 토큰만들기!!!!!!!!!!!!!!
     */
    @PostMapping("/token/refresh") //인증,권한이 필요한 uri
    @ApiOperation(value = "토큰 재발급 api", notes = "엑세스 토큰이 만료되었을때 리프레시 토큰으로 요청하게 되면 엑세스,리프레시 토큰을 재발급해줍니다. " +
            "리프레시 토큰이 정상적이지 않거나 기한이 만료되었으면 재로그인을 해야합니다.")
    public DefaultResponse recreateToken(@RequestParam("userId") Long userId, @RequestHeader("refreshToken") String refreshToken) {

        //토큰 검증후 엑세스 토큰 재생성
        JwtToken jwtToken = jwtService.validRefreshToken(userId, refreshToken);

        return new DefaultResponse("200", "토큰이 재발급되었습니다.",jwtToken);
    }

}
