package AMS.AMSsideproject.web.apiController.refreshToken;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import AMS.AMSsideproject.web.auth.jwt.service.JwtService;
import AMS.AMSsideproject.web.response.defaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Api(tags = "리프레시 토큰 관련 api")
public class RefreshTokenController {

    private final JwtService jwtService;
    private final UserService userService;


    @GetMapping("/{nickname}/refreshToken") //인증,권한이 필요한 uri
    @ApiOperation(value = "토큰 재발급 api", notes = "엑세스 토큰이 만료되었을때 리프레시토큰으로 요청하게 되면 엑세스,리프레시 토큰을 재발급해줍니다.")
    public defaultResponse recreateToken(@PathVariable("nickname") String nickname, @RequestHeader("refreshToken") String refreshToken) {

        User findUser = userService.findUserByNickName(nickname);
        JwtToken recreateToken = jwtService.validRefreshTokenAndRecreateToken(refreshToken, findUser.getUser_id());

        return new defaultResponse("200", "토큰이 재발급되었습니다.",recreateToken);
    }

}
