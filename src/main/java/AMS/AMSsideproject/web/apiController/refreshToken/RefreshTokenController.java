package AMS.AMSsideproject.web.apiController.refreshToken;

import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import AMS.AMSsideproject.web.auth.jwt.service.JwtService;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.response.DataResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/codebox")
@RequiredArgsConstructor
@Api(tags = "리프레시 토큰 관련 api")
public class RefreshTokenController {

    private final JwtService jwtService;

    @PostMapping("/refreshToken") //인증,권한이 필요한 uri
    @ApiOperation(value = "토큰 재발급 api", notes = "엑세스 토큰이 만료되었을때 리프레시 토큰으로 요청하게 되면 엑세스,리프레시 토큰을 재발급해줍니다. " +
            "리프레시 토큰이 정상적이지 않거나 기한이 만료되었으면 재로그인을 요청합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message = "엑세스,리프레시 토큰 재생성 성공"),
            @ApiResponse(code=400, message = "리프레시토큰이 없거나 잘못된 값 또는 유효기간이 만료되었음 다시 로그인 해야함", response = BaseErrorResult.class)
    })
    public DataResponse<JwtToken> recreateToken(@RequestHeader(JwtProperties.REFRESH_HEADER_STRING) String refreshToken) {

        //리프레시토큰 검증을 인터셉터에서 다 받았기 때문에 해당 컨트롤러가 호룰되는 것은 엑세스 토큰을 재발급 받아야되는 경우뿐이다!
        JwtToken jwtToken = jwtService.recreateTokenUsingTokenInfo(refreshToken);

        return new DataResponse<>("200", "토큰이 재발급되었습니다.",jwtToken);
    }

}
