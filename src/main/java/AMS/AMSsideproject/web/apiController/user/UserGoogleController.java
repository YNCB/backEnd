package AMS.AMSsideproject.web.apiController.user;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.auth.jwt.service.JwtService;
import AMS.AMSsideproject.web.dto.user.UserDto;
import AMS.AMSsideproject.web.dto.user.KakaoUserJoinDto;
import AMS.AMSsideproject.web.exception.UserNullException;
import AMS.AMSsideproject.web.oauth.provider.profile.GoogleProfile;
import AMS.AMSsideproject.web.oauth.provider.token.GoogleToken;
import AMS.AMSsideproject.web.oauth.service.GoogleService;
import AMS.AMSsideproject.web.response.defaultResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ams")
@Api(tags = "사용자 관련 api - google")
public class UserGoogleController {

    private final GoogleService googleService;
    private final UserService userService;
    private final JwtService jwtService;

    //인가코드 받아 회원가입을 처리하는 부분
    @GetMapping("/join/token/google")
    @ApiOperation(value = "인가코드를 받아 회원가입을 진행하는 api - google", notes = "인가코드를 받는 api 입니다. 엑세스토큰, " +
            "사용자 정보를 받아오게 되고 서비스의 회원가입을 진행합니다. 이미 회원가입한 사용자이면 회원가입 진행하지 않습니다." +
            "성공시 -> /ams/join api 로 추가 데이터와 함께 호출하면 됩니다. ")
    public defaultResponse GoogleJoinCheck(@RequestParam("code")String code) throws JsonProcessingException {

        //엑세스 토큰 받기
        GoogleToken googleToken = googleService.getAccessToken(code);

        //사용자 정보 받기
        GoogleProfile userProfile = googleService.getUserProfile(googleToken.getAccess_token());

        /**
         * 회원가입하지않은 사용자는 -> 회원가입시키기
         * 회원가입한 사용자는 -> 회원가입했다는 응답
         */
        try { //이미 회원가입 한 user
            User findUser = userService.findUserById(userProfile.getId());

            UserDto userJoinedDto = UserDto.builder()
                    .user_id(findUser.getUser_id())
                    .id(findUser.getId())
                    .nickname(findUser.getNickname())
                    .build();

            return new defaultResponse("200", "이미 회원가입한 사용자입니다.",userJoinedDto);

        }catch(UserNullException e ) { //회원가입이 필요한 user
            KakaoUserJoinDto userJoinDto = KakaoUserJoinDto.builder()
                    .id(userProfile.id)
                    .password(userProfile.id) //소셜 로그인은 비밀번호가 중요하지 않으니 그냥 세팅
                    .nickname(userProfile.kakao_account.profile.nickname)
                    .birth(userProfile.kakao_account.birthday)
                    .email(userProfile.kakao_account.email)
                    .social_type("Kakao")
                    .build();

            return new defaultResponse("200", "회원가입을 진행합니다.", userJoinDto);
        }

    }

    //카카오 로그인 처리 부분
    @GetMapping("/login/google")
    @ApiOperation(value = "")
    public defaultResponse GoogleLogin(@RequestParam("code") String code) throws JsonProcessingException {

        return null;
    }
}
