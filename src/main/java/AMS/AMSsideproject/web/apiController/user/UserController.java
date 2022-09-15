package AMS.AMSsideproject.web.apiController.user;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.user.requestDto.RequestValidNickName;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.auth.jwt.service.JwtService;
import AMS.AMSsideproject.web.exhanler.ErrorResult;
import AMS.AMSsideproject.web.response.BaseResponse;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.user.ResponseAuthCode;
import AMS.AMSsideproject.web.responseDto.user.ResponseValidNickName;
import AMS.AMSsideproject.web.apiController.user.requestDto.RequestEmailAuthDto;
import AMS.AMSsideproject.web.responseDto.user.UserDto;
import AMS.AMSsideproject.web.responseDto.user.UserEditDto;
import AMS.AMSsideproject.web.service.email.EmailService;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserEditForm;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserJoinForm;
import AMS.AMSsideproject.web.response.DefaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/codebox")
@Api(tags = "사용자 관련 api")
public class UserController {

    private final UserService userService;
    private final EmailService emailService;
    private final JwtService jwtService;

    //실제 회원가입을 진행하는 부분
    @PostMapping("/join")
    @ApiOperation(value = "실제 회원가입을 진행하는 api", notes = "실제 회원가입을 진행합니다.")
    public BaseResponse Join(@RequestBody UserJoinForm userJoinForm) {

        User joinUser = userService.join(userJoinForm);
        //회원가입완료는 필요없지 않나
//        UserDto joinUserDto = UserDto.builder()
//                .user_id(joinUser.getUser_id())
//                .email(joinUser.getEmail())
//                .nickname(joinUser.getNickname())
//                .build();
        return new BaseResponse("200", "회원가입이 정상적으로 완료되었습니다.");
    }

    // 회원가입시 닉네임 중복 검사하는 부분
    @PostMapping("/join/validNickName")
    @ApiOperation(value = "추가정보 회원가입시 닉네임 중복검사하는 api", notes = "추가정보 회원가입 시 회원 닉네임에 대해서 중복 검사를 합니다.")
    @ApiResponses( {
            @ApiResponse(code=200, message = "닉네임 사용가능") ,
            @ApiResponse(code=400, message = "닉네임 중복", response = ErrorResult.class)
    })
    public DataResponse<ResponseValidNickName> ValidDuplicateNickName(@RequestBody RequestValidNickName validNickName) {

        //이거도 굳이 리턴할필요가 있나?! 닉네임을?!
        String nickName= userService.validDuplicateUserNickName(validNickName.nickName);
        return new DataResponse<>("200", "사용 가능한 닉네임 입니다.", new ResponseValidNickName(nickName));
    }

    // 회원가입시 이메일 인증 하는 부분
    @PostMapping("/join/mailConfirm")
    @ApiOperation(value = "회원가입시 이메일 인증하는 api", notes = "전달한 이메일로 인증코드를 전송합니다. 반환값으로 인증코드를 반환해줍니다.")
    public DataResponse<ResponseAuthCode> mailConfirm(@RequestBody RequestEmailAuthDto emailDto) throws MessagingException, UnsupportedEncodingException {

        String authCode = emailService.sendEmail(emailDto.email);
        return new DataResponse<>("200", "인증코드를 전송하였습니다. 생성된 인증코드 입니다.", new ResponseAuthCode(authCode));
    }


    //회원 정보 수정 -> 권한 체크 필요한 기능
    @GetMapping("/setting")
    @ApiOperation(value = "회원 정보 수정 api", notes = "회원 수정을 위해 사용자 정보를 제공해주는 api 입니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = ErrorResult.class),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class)
    })
    public DataResponse<UserEditDto> UserEditForm(@RequestHeader(JwtProperties.HEADER_STRING) String accessToken) {

        //Token 정보에서 User 찾기
        User findUser = jwtService.findUserToToken(accessToken);
        UserEditDto userEditDto = UserEditDto.createUserEditDto(findUser);
        return new DataResponse<>("200", "회원 정보입니다.", userEditDto);
    }
    @PutMapping("/setting")
    @ApiOperation(value = "회원 정보 수정 api", notes = "수정된 회원정보를 받는 api 입니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = ErrorResult.class),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class)
    })
    public DataResponse<UserDto> UserEdit(@RequestHeader(JwtProperties.HEADER_STRING) String accessToken, @RequestBody UserEditForm userEditForm) {

        //해당 api를 호출 하기전에  클라이언트에서 "닉네임 중복검사 api 호출" 해야됌

        //Token 정보에서 User 찾기
        User findUser = jwtService.findUserToToken(accessToken);

        User updateUser = userService.update(findUser.getUser_id(), userEditForm);

        /**
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * 회원 정보가바꼈으니 JWT 토큰 값도 안바꿔도 되나?!
         * 안바꿔도 된다 - 이걸 걱정 하는 이유가 뒤에 게시물 등록, 수정을 다른 사람이 못하게 막으려고 요청한 uri 에 nickname 파싱해서 JWT 토큰에 있는 사용자 정보
         *               랑 비교해서 본인이 아니면 reject 하려고 하는데 JWT토큰에서 그냥 userId 꺼내서 userId 로 사용자 찾으면 되니깐.!
         *               단점) 그러면 위의 검증하는 인터셉터에서 검증이 필요한 uri 호출시 받드시 쿼리문 하나는 고정적으로 나가는데 영향없겠나?!!!
         *
         * 바꿔야 된다 - 회원수정 보다는 게시물 등록, 수정을 더많이 호출하잖아 그러면 회원수정에서 토큰도 새로 생성하는게 쿼리문 더 줄어들지 않을까
         *             회원수정일때 엑세스,리프레시 생성하면 쿼리문하나 발생(리프레시 토큰 업데이트).
         */
        return new DataResponse<>("200", "회원 수정이 완료되었습니다", new UserDto(updateUser.getUser_id(), updateUser.getNickname()));
    }



}
