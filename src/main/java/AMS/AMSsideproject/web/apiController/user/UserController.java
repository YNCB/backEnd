package AMS.AMSsideproject.web.apiController.user;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.user.form.UserEdit;
import AMS.AMSsideproject.web.apiController.user.form.UserEditForm;
import AMS.AMSsideproject.web.apiController.user.form.UserJoinForm;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import AMS.AMSsideproject.web.auth.jwt.service.JwtService;
import AMS.AMSsideproject.web.dto.user.UserJoinDto;
import AMS.AMSsideproject.web.dto.user.UserDto;
import AMS.AMSsideproject.web.dto.user.UserLoginDto;
import AMS.AMSsideproject.web.exception.DuplicationUserNickname;
import AMS.AMSsideproject.web.exception.UserNullException;
import AMS.AMSsideproject.web.oauth.provider.profile.KakaoProfile;
import AMS.AMSsideproject.web.oauth.provider.token.KakaoToken;
import AMS.AMSsideproject.web.oauth.service.KakaoService;
import AMS.AMSsideproject.web.response.defaultResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * Exception 같은 경우에는 try~catch 구문을 쓰지말고 "@controllerAdvice"로 공통 처리하는게 맞지?
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/ams")
@Api(tags = "사용자 관련 api")
public class UserController {

    private final KakaoService kakaoService;
    private final UserService userService;
    private final JwtService jwtService;

    //인가코드 받아 회원가입을 처리하는 부분
    @GetMapping("/join/token/kakao")
    @ApiOperation(value = "인가코드를 받아 회원가입을 진행하는 api - kakao", notes = "인가코드를 받는 api 입니다. 엑세스토큰, " +
            "사용자 정보를 받아오게 되고 서비스의 회원가입을 진행합니다. 이미 회원가입한 사용자이면 회원가입 진행하지 않습니다." +
            "성공시 -> /ams/join uri로 추가 데이터와 함께 호출하면 됩니다. ")
    public defaultResponse KakaoJoinCheck(@RequestParam("code")String code) throws JsonProcessingException {

        //엑세스 토큰 받기
        KakaoToken kakaoToken = kakaoService.getAccessToken(code);

        //사용자 정보 받기
        KakaoProfile userProfile = kakaoService.getUserProfile(kakaoToken.getAccess_token());

        /**
         * 회원가입하지않은 사용자는 -> 회원가입시키기
         * 회원가입한 사용자는 -> 회원가입했다는 응답
         */
        try { //이미 회원가입 한 user
            User findUser = userService.findUserBySocialId(userProfile.getId());

            UserDto userJoinedDto = UserDto.builder()
                    .user_id(findUser.getUser_id())
                    .social_id(findUser.getSocial_id())
                    .nickname(findUser.getNickname())
                    .build();

            return new defaultResponse("200", "이미 회원가입한 사용자입니다.",userJoinedDto);

        }catch(UserNullException e ) { //회원가입이 필요한 user
            UserJoinDto userJoinDto = UserJoinDto.builder()
                    .social_id(userProfile.id)
                    .nickname(userProfile.kakao_account.profile.nickname)
                    .birth(userProfile.kakao_account.birthday)
                    .email(userProfile.kakao_account.email)
                    .social_type("Kakao")
                    .build();

            return new defaultResponse("200", "회원가입을 진행합니다.", userJoinDto);
        }

    }

    //실제 회원가입을 진행하는 부분
    @PostMapping("/join")
    @ApiOperation(value = "실제 회원가입을 진행하는 api", notes = "실제 회원가입을 진행합니다. 닉네임 중복 여부도 검사합니다. ")
    public defaultResponse Join(@RequestBody UserJoinForm userJoinForm) {

        try { //정상적인 회원가입
            User joinUser = userService.join(userJoinForm);
            UserDto joinUserDto = UserDto.builder()
                    .social_id(joinUser.getSocial_id())
                    .nickname(joinUser.getNickname())
                    .build();

            return new defaultResponse("200", "회원가입이 정상적으로 되었습니다", joinUserDto);
        } catch (DuplicationUserNickname e) { //중복된 닉네임을 입력한 경우

            return new defaultResponse("200", "중복된 닉네임 입니다", userJoinForm);
        }

    }

    //카카오 로그인 처리 부분
    @GetMapping("/login/kakao")
    @ApiOperation(value = "로그인을 처리하는 api - kakao", notes = "회원가입을 한 사용자가 소셜로그인을 하면 로그인 처리 ," +
            " 회원가입을 하지않는 사용자가 소셜로그인을 하면 회원가입을 하라는 요청을 보냅니다." +
            " 성공시 -> /ams/{nickname} uri 호출 ")
    public defaultResponse KakaoLogin(@RequestParam("code") String code) throws JsonProcessingException {

        //엑세스 토큰 받기
        KakaoToken kakaoToken = kakaoService.getAccessToken(code);

        //사용자 정보 받기
        KakaoProfile userProfile = kakaoService.getUserProfile(kakaoToken.getAccess_token());

        //회원가입을 한 이용자 /
        User findUser = userService.findUserBySocialId(userProfile.id);

        //토큰(access, refresh) 생성
        JwtToken jwtToken = jwtService.createAndSaveToken(findUser.getUser_id(), findUser.getNickname(), findUser.getRole());
        UserLoginDto userLoginDto = UserLoginDto.builder()
                .user_Id(findUser.getUser_id())
                .nickname(findUser.getNickname())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();

        return new defaultResponse("200", "로그인을 성공하였습니다. 토큰이 발급되었습니다.", userLoginDto); // -> redirect:/api/{nickname}
    }


    //회원 정보 수정
    @GetMapping("/edit")
    @ApiOperation(value = "회원 정보 수정 api", notes = "아직 구현 중... ")
    public defaultResponse UserEditForm(@RequestBody UserEdit userEdit) {

        User findUser = userService.findUserByUserId(userEdit.getUserId());

        /**
         * 요청으로 userid, nickname 받기 (useid만 못받으니)
         * 응답으로 수정가능한 사용자 정보 dto return
         */

        return null;

    }
    @PutMapping("/edit")
    @ApiOperation(value = "회원 정보 수정 api", notes = "아직 구현 중...")
    public defaultResponse UserEdit(@RequestBody UserEditForm userEditForm) {

        /**
         * 요청으로 userId를 포함한 수정한 사용자 정보 받기
         * 응답으로 userId, nickname 해주기
         */

        return null;
    }


    /**
     * 테스트테스트테스트
     * merge 테스트
     * 브렌치 테스트트
     *
    */

}
