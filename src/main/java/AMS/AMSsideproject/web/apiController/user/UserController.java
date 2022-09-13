package AMS.AMSsideproject.web.apiController.user;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.user.requestDto.ValidNickName;
import AMS.AMSsideproject.web.service.email.EmailAuthRequestDto;
import AMS.AMSsideproject.web.service.email.EmailService;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserEditForm;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserJoinForm;
import AMS.AMSsideproject.web.responseDto.user.UserDto;
import AMS.AMSsideproject.web.response.DefaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    //실제 회원가입을 진행하는 부분
    @PostMapping("/join")
    @ApiOperation(value = "실제 회원가입을 진행하는 api", notes = "실제 회원가입을 진행합니다. ")
    public DefaultResponse Join(@RequestBody UserJoinForm userJoinForm) {

        User joinUser = userService.join(userJoinForm);

        UserDto joinUserDto = UserDto.builder()
                .user_id(joinUser.getUser_id())
                .email(joinUser.getEmail())
                .nickname(joinUser.getNickname())
                .build();

        return new DefaultResponse("200", "회원가입이 정상적으로 되었습니다", joinUserDto);
    }

    // 회원가입시 닉네임 중복 검사하는 부분
    @PostMapping("/join/validNickName")
    @ApiOperation(value = "추가정보 회원가입시 닉네임 중복검사하는 api", notes = "추가정보 회원가입 시 회원 닉네임에 대해서 중복 검사를 합니다.")
    public DefaultResponse ValidDuplicateNickName(@RequestBody ValidNickName validNickName) {

        String nickName= userService.validDuplicateUserNickName(validNickName.nickName);

        return new DefaultResponse("200", "사용 가능한 닉네임 입니다.", nickName);
    }

    // 회원가입시 이메일 인증 하는 부분
    @PostMapping("/join/mailConfirm")
    @ApiOperation(value = "회원가입시 이메일 인증하는 api", notes = "전달한 이메일로 인증코드를 전송합니다. 반환값으로 인증코드를 반환해줍니다.")
    public DefaultResponse mailConfirm(@RequestBody EmailAuthRequestDto emailDto) throws MessagingException, UnsupportedEncodingException {

        String authCode = emailService.sendEmail(emailDto.email);
        return new DefaultResponse("200", "인증코드를 전송하였습니다. 생성된 인증코드 입니다.", authCode);
    }












    //회원 정보 수정 -> 권한 체크 필요한 기능
    @GetMapping("/setting")
    @ApiOperation(value = "회원 정보 수정 api", notes = "회원 수정을 위해 사용자 정보를 제공해주는 api 입니다.")
    public DefaultResponse UserEditForm() {


        /**
         * 요청으로 userid, nickname 받기 (useid만 못받으니)
         * 응답으로 수정가능한 사용자 정보 dto return
         */

        return null;

    }
    @PutMapping("/setting")
    @ApiOperation(value = "회원 정보 수정 api", notes = "수정된 회원정보를 받는 api 입니다.")
    public DefaultResponse UserEdit(@RequestBody UserEditForm userEditForm) {

        /**
         * 요청으로 userId를 포함한 수정한 사용자 정보 받기
         * 응답으로 userId, nickname 해주기
         */

        return null;
    }



}
