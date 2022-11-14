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
    public DataResponse<UserJoinForm1> Step1Join(@Validated @RequestBody UserJoinForm1 userJoinForm) {
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
    public BaseResponse Step2Join(@Validated @RequestBody UserJoinForm2 userJoinForm) {

        User joinUser = userService.join(userJoinForm);
        return new BaseResponse("200", "회원가입이 정상적으로 완료되었습니다.");
    }

    // 회원가입시 닉네임 중복 검사하는 부분
    // "406" error 정의
    @PostMapping("/join/validNickName")
    @ApiOperation(value = "추가정보 회원가입시 닉네임 중복검사하는 api", notes = "추가정보 회원가입 시 회원 닉네임에 대해서 중복 검사를 합니다.")
    @ApiResponses( {
            @ApiResponse(code=200, message = "닉네임 사용가능") ,
            @ApiResponse(code=400, message = "닉네임 중복", response = ValidDuplicateNickName_400.class ),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<ValidNickNameDto> ValidDuplicateNickName(@Validated @RequestBody ValidNickNameDto validNickName) {

        //이거도 굳이 리턴할필요가 있나?! 닉네임을?!
        String nickName= userService.validDuplicateUserNickName(validNickName.nickName);
        return new DataResponse<>("200", "사용 가능한 닉네임 입니다.", new ValidNickNameDto(nickName));
    }

    // 회원가입시 이메일 인증 하는 부분
    // "406" error 정의
    @PostMapping("/join/mailConfirm")
    @ApiOperation(value = "회원가입시 이메일 인증하는 api", notes = "전달한 이메일로 인증코드를 전송합니다. 반환값으로 인증코드를 반환해줍니다.")
    @ApiResponses({
            @ApiResponse(code=200, message = "인증코드 전송완료"),
            @ApiResponse(code=400, message = "이미 회원가입된 사용자", response = BaseErrorResult.class),
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
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<UserEditDto> UserEditForm(@RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken) {

        //Token 정보에서 User 찾기
        User findUser = jwtService.findUserToToken(accessToken);
        UserEditDto userEditDto = UserEditDto.createUserEditDto(findUser);
        return new DataResponse<>("200", "회원 정보입니다.", userEditDto);
    }

    @PutMapping("/setting")
    @ApiOperation(value = "회원 정보 수정 api", notes = "수정된 회원정보를 받는 api 입니다. 회원 정보가 수정되면 JWT토큰안에 회원 정보도 수정해야되기 때문에 토큰도 재발급")
    @ApiResponses({
            @ApiResponse(code=200, message="정상 호출"),
            @ApiResponse(code=401, message ="JWT 토큰이 토큰이 없거나 정상적인 값이 아닙니다.", response = BaseErrorResult.class),
            @ApiResponse(code=201, message = "엑세스토큰 기한만료", response = BaseResponse.class),
            @ApiResponse(code=500, message = "Internal server error", response = BaseErrorResult.class)
    })
    public DataResponse<UserEditSuccessDto> UserEdit(@RequestHeader(JwtProperties.ACCESS_HEADER_STRING) String accessToken, @RequestBody UserEditForm userEditForm) {

        //해당 api를 호출 하기전에  클라이언트에서 "닉네임 중복검사 api 호출" 해야됌

        //Token 정보에서 User 찾기
        Long findUserId = jwtProvider.getUserIdToToken(accessToken);

        User updateUser = userService.update(findUserId, userEditForm);

        //토큰 새로 발급
        JwtToken jwtToken = jwtService.createAndSaveToken(updateUser.getUser_id(), updateUser.getNickname(), updateUser.getRole());
        /**
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
         * 회원 정보가바꼈으니 JWT 토큰 값도 안바꿔도 되나?!
         * 안바꿔도 된다 - 이걸 걱정 하는 이유가 뒤에 게시물 등록, 수정을 다른 사람이 못하게 막으려고 요청한 uri 에 nickname 파싱해서 JWT 토큰에 있는 사용자 정보
         *               랑 비교해서 본인이 아니면 reject 하려고 하는데 JWT토큰에서 그냥 userId 꺼내서 userId 로 사용자 찾으면 되니깐.!
         *               단점) 그러면 위의 검증하는 인터셉터에서 검증이 필요한 uri 호출시 받드시 쿼리문 하나는 고정적으로 나가는데 영향없겠나?!!!
         *
         * 바꿔야 된다 - 회원수정 보다는 게시물 등록, 수정을 더많이 호출하잖아 그러면 회원수정에서 토큰도 새로 생성하는게 쿼리문 더 줄어들지 않을까
         *             회원수정일때 엑세스,리프레시 생성하면 쿼리문하나 발생(리프레시 토큰 업데이트).
         *             게시물 등록할때 JWT토큰의 닉네임을 가지고 "권한"이 있는지 체크하니깐 JWT토큰안에 닉네임을 사용하니.
         *             만약 업데이트 하지않으면 JWT토큰안에 있는 닉네임이 "회원수정" 되었으면 구버전의 닉네임이 있으니 게시물 작성할때 쿼리문 하나더 발생하니.!!
         *
         */
        return new DataResponse<>("200", "회원 수정이 완료되었습니다. 토큰이 재발급되었습니다.",
                new UserEditSuccessDto(updateUser.getUser_id(), updateUser.getNickname(),
                        jwtToken.getAccessToken(), jwtToken.getRefreshToken(), jwtToken.getMy_session()));

    }



}
