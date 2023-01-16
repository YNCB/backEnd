package AMS.AMSsideproject.web.apiController.user;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.user.requestDto.*;
import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import AMS.AMSsideproject.web.auth.jwt.service.JwtProvider;
import AMS.AMSsideproject.web.auth.jwt.service.JwtService;
import AMS.AMSsideproject.web.exhandler.BaseErrorResult;
import AMS.AMSsideproject.web.response.BaseResponse;
import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.user.ResponseAuthCode;
import AMS.AMSsideproject.web.responseDto.user.UserEditSuccessDto;
import AMS.AMSsideproject.web.responseDto.user.UserEditDto;
import AMS.AMSsideproject.web.service.email.EmailService;
import AMS.AMSsideproject.web.swagger.userController.Join_406;
import AMS.AMSsideproject.web.swagger.userController.ValidDuplicateNickName_400;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
    private final JwtProvider jwtProvider;

    // 1차 회원가입
    @PostMapping("/join1")
    @ApiOperation(value = "1차 회원가입을 진행하는 api", notes = "1차 회원가입을 진행합니다.")
    @ApiResponses({
            @ApiResponse(code=200, message = "1차 회원가입 성공"),
            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<UserJoinForm1> join1(@Validated @RequestBody UserJoinForm1 userJoinForm) {

        return new DataResponse<>("200","1차 회원가입이 완료되었습니다.",userJoinForm);
    }

    //2차 회원가입 - 실제 회원가입하는 부분
    @PostMapping("/join2")
    @ApiOperation(value = "2차 회원가입을 진행하는 api", notes = "2차 회원가입을 진행합니다. 실제 회원가입이 완료됩니다. " +
            "일반회원가입일 경우에는 social_type key 값을 Basic 으로 주시면 됩니다.")
    @ApiResponses({
            @ApiResponse(code=200, message = "2차 회원가입 성공"),
            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public BaseResponse join2(@Validated @RequestBody UserJoinForm2 userJoinForm) {

        User joinUser = userService.join(userJoinForm);
        return new BaseResponse("200", "회원가입이 정상적으로 완료되었습니다.");
    }

    // 회원가입시 닉네임 중복 검사하는 부분
    @PostMapping("/join/validNickName")
    @ApiOperation(value = "추가정보 회원가입시 닉네임 중복검사하는 api", notes = "추가정보 회원가입 시 회원 닉네임에 대해서 중복 검사를 합니다.")
    @ApiResponses( {
            @ApiResponse(code=200, message = "닉네임 사용가능") ,
            @ApiResponse(code=400, message = "닉네임 중복", response = ValidDuplicateNickName_400.class ),
            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<ValidNickNameDto> ValidDuplicateNickName(@Validated @RequestBody ValidNickNameDto validNickName) {

        //이거도 굳이 리턴할필요가 있나?! 닉네임을?!
        String nickName= userService.validDuplicateUserNickName(validNickName.nickname);
        return new DataResponse<>("200", "사용 가능한 닉네임 입니다.", new ValidNickNameDto(nickName));
    }

    // 회원가입시 이메일 인증 하는 부분
    @PostMapping("/join/mailConfirm")
    @ApiOperation(value = "회원가입시 이메일 인증하는 api", notes = "전달한 이메일로 인증코드를 전송합니다. 반환값으로 인증코드를 반환해줍니다.")
    @ApiResponses({
            @ApiResponse(code=200, message = "인증코드 전송완료"),
            @ApiResponse(code=400, message = "이미 회원가입된 사용자", response = BaseErrorResult.class),
            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<ResponseAuthCode> mailConfirm(@Validated @RequestBody RequestEmailAuthDto emailDto) throws MessagingException, UnsupportedEncodingException {

        String authCode = emailService.sendEmail(emailDto.email);
        return new DataResponse<>("200", "인증코드를 전송하였습니다. 생성된 인증코드 입니다.", new ResponseAuthCode(authCode));
    }

    //회원 정보 수정 -> 권한 체크 필요한 기능
    @GetMapping("/setting")
    @ApiOperation(value = "회원 정보 수정 api", notes = "회원 수정을 위해 사용자 정보를 제공해주는 api 입니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=412, message = "로그아웃 처리된 엑세스 토큰입니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰",required = true)
    public DataResponse<UserEditDto> UserEditForm(@RequestHeader(JwtProperties.ACCESS_HEADER_STRING)String accessToken) {

        //Token 정보에서 User 찾기
        User findUser = jwtService.findUserToToken(accessToken);
        UserEditDto userEditDto = UserEditDto.createUserEditDto(findUser);
        return new DataResponse<>("200", "회원 정보입니다.", userEditDto);
    }

    @PutMapping("/setting")
    @ApiOperation(value = "회원 정보 수정 api", notes = "수정된 회원정보를 받는 api 입니다. " +
            "닉네임을 변경하려면 닉네임 중복 검사 api 이전에 호출해야 됩니다. " +
            "회원 정보가 수정되면 JWT 토큰 회원 정보도 수정해야되기 때문에 토큰도 재발급됩니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=406, message = "각 키값 조건 불일치", response = Join_406.class),
            @ApiResponse(code=412, message = "로그아웃 처리된 엑세스 토큰입니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰",required = true)
    public DataResponse<UserEditSuccessDto> UserEdit(@RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken,
                                                     @Validated @RequestBody UserEditForm userEditForm) {

        //Token 정보에서 User 찾기
        Long findUserId = jwtProvider.getUserIdToToken(accessToken);

        User updateUser = userService.update(findUserId, userEditForm);

        //토큰 새로 발급
        JwtToken jwtToken = jwtService.createAndSaveToken(updateUser.getUser_id(), updateUser.getNickname(), updateUser.getRole());

        UserEditSuccessDto dto = new UserEditSuccessDto(updateUser.getUser_id(), updateUser.getNickname(), jwtToken);
        return new DataResponse<>("200", "회원 수정이 완료되었습니다. 토큰이 재발급되었습니다.", dto);
    }

    /**
     * refreshToken도 삭제해야된다!!!!!
     */
    //인증,권한 검사하는 api-토큰의 유효기간이 끝이 나면 어차피 로그아웃 처리하지 않아도 사용못하니
    @GetMapping("/logout")
    @ApiOperation(value = "로그아웃 api", notes = "엑세스 토큰을 블랙리스트 처리니다.")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    @ApiImplicitParam(name = JwtProperties.ACCESS_HEADER_STRING, value = "엑세스 토큰", required = true)
    public BaseResponse userLogout(@RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken){

        userService.logout(accessToken);

        return new BaseResponse("200", "로그아웃이 되었습니다");
    }

}
