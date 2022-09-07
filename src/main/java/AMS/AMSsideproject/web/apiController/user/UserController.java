package AMS.AMSsideproject.web.apiController.user;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.user.form.requestUserEdit;
import AMS.AMSsideproject.web.apiController.user.form.UserEditForm;
import AMS.AMSsideproject.web.apiController.user.form.UserJoinForm;
import AMS.AMSsideproject.web.dto.user.UserDto;
import AMS.AMSsideproject.web.response.defaultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ams")
@Api(tags = "사용자 관련 api")
public class UserController {

    private final UserService userService;

    //실제 회원가입을 진행하는 부분
    @PostMapping("/join")
    @ApiOperation(value = "실제 회원가입을 진행하는 api", notes = "실제 회원가입을 진행합니다. ")
    public defaultResponse Join(@RequestBody UserJoinForm userJoinForm) {

        User joinUser = userService.join(userJoinForm);

        UserDto joinUserDto = UserDto.builder()
                .user_id(joinUser.getUser_id())
                .id(joinUser.getId())
                .nickname(joinUser.getNickname())
                .build();

        return new defaultResponse("200", "회원가입이 정상적으로 되었습니다", joinUserDto);
    }

    /**
     * 회원가입에서 아이디, 닉네임 중복검사하지 않고 보내게 되면 어트케 처리되나?!! 프론트측에서 했는지 안했는지 판별하나?!!!
     * 폼데이터로 id, nickname 받는게 되나 (프론트엔드 측에서)!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    // 회원가입시 아이디 중복 검사하는 부분
    @PostMapping("/join/validId")
    @ApiOperation(value = "회원가입시 아이디 중복검사하는 api", notes = "회원가입 시 회원 아이디에 대해서 중복 검사를 합니다.")
    public defaultResponse ValidDuplicateId (@RequestParam("id") String id) {

        String validId = userService.validDuplicateUserId(id);

        return new defaultResponse("200", "사용 가능한 아이디 입니다.", validId);

    }

    // 회원가입시 닉네임 중복 검사하는 부분
    @PostMapping("/join/validNickName")
    @ApiOperation(value = "회원가입시 닉네임 중복검사하는 api", notes = "회원가입 시 회원 닉네임에 대해서 중복 검사를 합니다.")
    public defaultResponse ValidDuplicateNickName(@RequestParam("nickname") String nickName) {

        String validNickName = userService.validDuplicateUserNickName(nickName);

        return new defaultResponse("200", "사용 가능한 닉네임 입니다.", validNickName);
    }


    //회원 정보 수정 -> 권한 체크 필요한 기능
    @GetMapping("/setting")
    @ApiOperation(value = "회원 정보 수정 api", notes = "회원 수정을 위해 사용자 정보를 제공해주는 api 입니다.")
    public defaultResponse UserEditForm(@RequestBody requestUserEdit userEdit) {

        User findUser = userService.findUserByUserId(userEdit.getUserId());

        /**
         * 요청으로 userid, nickname 받기 (useid만 못받으니)
         * 응답으로 수정가능한 사용자 정보 dto return
         */

        return null;

    }
    @PutMapping("/setting")
    @ApiOperation(value = "회원 정보 수정 api", notes = "수정된 회원정보를 받는 api 입니다.")
    public defaultResponse UserEdit(@RequestBody UserEditForm userEditForm) {

        /**
         * 요청으로 userId를 포함한 수정한 사용자 정보 받기
         * 응답으로 userId, nickname 해주기
         */

        return null;
    }



}
