package AMS.AMSsideproject.web.apiController.refreshToken;

import AMS.AMSsideproject.web.jwt.JwtProperties;
import AMS.AMSsideproject.web.jwt.JwtToken;
import AMS.AMSsideproject.web.jwt.service.JwtService;
import AMS.AMSsideproject.web.exhandler.dto.BaseErrorResult;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.user.UserTokenDto;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/codebox")
@RequiredArgsConstructor
@Api(tags = "리프레시 토큰 관련 api")
public class RefreshTokenController {

    private final JwtService jwtService;

    @PostMapping("/refreshToken") //인증,권한이 필요한 uri
    @ApiOperation(value = "토큰 재발급 api", notes = "엑세스 토큰이 만료되었을때 리프레시 토큰으로 요청하게 되면 엑세스,리프레시 토큰을 재발급해줍니다.")
    @ApiResponses({
            @ApiResponse(code=200, message = "토큰이 재발급되었습니다."),
            @ApiResponse(code=401, message = "인증에 실패하였습니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParam(name = JwtProperties.REFRESH_HEADER_STRING, value = "리프레쉬 토큰",required = true)
    public DataResponse<UserTokenDto> recreateToken(@RequestHeader(JwtProperties.REFRESH_HEADER_STRING) String refreshToken) {

        JwtToken jwtToken = jwtService.reissue(refreshToken);

        UserTokenDto userTokenDto = new UserTokenDto(jwtToken);
        return new DataResponse<>("200", "토큰이 재발급되었습니다.",userTokenDto);
    }
}
