package AMS.AMSsideproject.web.apiController.user;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import AMS.AMSsideproject.web.auth.jwt.service.JwtService;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.oauth.provider.info.KakaoInfo;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.user.KakaoUserJoinDto;
import AMS.AMSsideproject.web.responseDto.user.UserLoginDto;
import AMS.AMSsideproject.web.exception.UserNullException;
import AMS.AMSsideproject.web.oauth.provider.profile.KakaoProfile;
import AMS.AMSsideproject.web.oauth.provider.token.KakaoToken;
import AMS.AMSsideproject.web.oauth.service.KakaoService;
import AMS.AMSsideproject.web.swagger.userKakaoController.KakaoLogin_200;
import AMS.AMSsideproject.web.swagger.userKakaoController.KakaoLogin_201;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/codebox")
@Api(tags = "사용자 관련 api - kakao")
public class UserKakaoController {

    private final KakaoService kakaoService;
    private final UserService userService;
    private final JwtService jwtService;

//    //인가코드 받아 회원가입을 처리하는 부분
//    @GetMapping("/join/token/kakao")
//    @ApiOperation(value = "인가코드를 받아 카카오 회원가입을 진행하는 api - kakao", notes = "인가코드를 받는 api 입니다. 엑세스토큰, " +
//            "사용자 정보를 받아오게 되고 카카오 회원가입을 진행합니다. 이미 카카오 회원가입한 사용자이면 회원가입 진행하지 않습니다." +
//            "성공시 -> /ams/join api 로 추가 데이터와 함께 호출하면 됩니다. ")
//    public DefaultResponse KakaoJoinCheck(@RequestParam("code")String code) throws JsonProcessingException {
//
//        //엑세스 토큰 받기
//        KakaoToken kakaoToken = kakaoService.getAccessToken(code);
//
//        //사용자 정보 받기
//        KakaoProfile userProfile = kakaoService.getUserProfile(kakaoToken.getAccess_token());
//
//        /**
//         * 회원가입하지않은 사용자는 -> 회원가입시키기
//         * 회원가입한 사용자는 -> 회원가입했다는 응답
//         */
//        try { //이미 회원가입 한 user
//            User findUser = userService.findUserByEmail(userProfile.getId());
//
//            UserDto userJoinedDto = UserDto.builder()
//                    .user_id(findUser.getUser_id())
//                    .email(findUser.getEmail())
//                    .nickname(findUser.getNickname())
//                    .build();
//
//            return new DefaultResponse("200", "이미 회원가입한 사용자입니다.",userJoinedDto);
//
//        }catch(UserNullException e ) { //회원가입이 필요한 user
//            KakaoUserJoinDto userJoinDto = KakaoUserJoinDto.builder()
//                    .id(userProfile.id)
//                    .password(userProfile.id) //소셜 로그인은 비밀번호가 중요하지 않으니 그냥 세팅
//                    .nickname(userProfile.kakao_account.profile.nickname)
//                    //.birth(userProfile.kakao_account.birthday)
//                    .email(userProfile.kakao_account.email)
//                    .social_type("Kakao")
//                    .build();
//
//            return new DefaultResponse("200", "회원가입을 진행합니다.", userJoinDto);
//        }
//    }

    //카카오 로그인 처리 부분
    @GetMapping("/login/token/kakao")
    @ApiOperation(value = "카카오 로그인을 처리하는 api - google", notes = "회원가입 한 사용자이면 카카오 로그인 처리 ," +
            " 회원가입을 하지않는 사용자이면 회원가입 진행(2차 회원가입), " +
            " 로그인 성공시 -> /codebox/{nickname} api 호출하면 됩니다. ")
    @ApiResponses({
            @ApiResponse(code=200, message = "로그인 성공", response =  KakaoLogin_200.class),
            @ApiResponse(code=201, message = "회원가입 진행",response = KakaoLogin_201.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParam(name = "code", value = "인가 코드", required = true)
    public DataResponse<?> KakaoLogin(@RequestParam("code") String code, HttpServletResponse response) throws JsonProcessingException {

        //엑세스 토큰 받기
        KakaoToken kakaoToken = kakaoService.getAccessToken(code);

        //사용자 정보 받기
        KakaoProfile userProfile = kakaoService.getUserProfile(kakaoToken.getAccess_token());

        //회원가입을 한 이용자인지 검증
        try {
            User findUser = userService.findUserByEmail(userProfile.kakao_account.email);

            //토큰(access, refresh) 생성
            JwtToken jwtToken = jwtService.createAndSaveToken(findUser.getUser_id(), findUser.getNickname(), findUser.getRole());

            UserLoginDto userLoginDto = new UserLoginDto(findUser.getUser_id(), findUser.getNickname(), jwtToken);

            return new DataResponse<>("200", "로그인을 성공하였습니다. 토큰이 발급되었습니다.", userLoginDto); // -> redirect:/api/{nickname}

        }catch (UserNullException e) {

            KakaoUserJoinDto userJoinDto = KakaoUserJoinDto.builder()
                    .email(userProfile.kakao_account.email)
                    .password(KakaoInfo.KakaoLoginPassWord) //소셜 로그인은 비밀번호가 중요하지 않으니 그냥 세팅
                    .nickname(userProfile.kakao_account.profile.nickname)
                    .social_type("Kakao")
                    .build();

            response.setStatus(HttpStatus.CREATED.value());
            return new DataResponse<>("201", "회원가입하지 않은 사용자입니다. 회원가입을 진행합니다.", userJoinDto);

        }
    }

}
