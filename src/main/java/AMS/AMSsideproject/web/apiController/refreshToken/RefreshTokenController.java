package AMS.AMSsideproject.web.apiController.refreshToken;

import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import AMS.AMSsideproject.web.auth.jwt.service.JwtService;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.user.UserTokenDto;
import io.swagger.annotations.*;
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
            @ApiResponse(code=401, message = "정상적이지 않은 토큰입니다. or 엑세스 토큰의 기한이 만료되었습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=412, message = "토큰이 없습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParam(name = JwtProperties.REFRESH_HEADER_STRING, value = "리프레쉬 토큰",required = true)
    public DataResponse<UserTokenDto> recreateToken(@RequestHeader(JwtProperties.REFRESH_HEADER_STRING) String refreshToken) {

        //리프레시토큰 검증을 인터셉터에서 다 받았기 때문에 해당 컨트롤러가 호출되는 것은 accessToken 만 재발급 받아야되는 경우뿐이다!!!
        JwtToken jwtToken = jwtService.recreateTokenUsingTokenInfo(refreshToken);

        UserTokenDto userTokenDto = new UserTokenDto(jwtToken);
        return new DataResponse<>("200", "토큰이 재발급되었습니다.",userTokenDto);
    }

}
