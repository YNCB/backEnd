package AMS.AMSsideproject.web.apiController.user.requestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinForm {

    /**
     * validation 적용하기 -> bingResult 쓰기
     */
    private String email;
    private String password;
    private String nickname;
    private String social_type; //google, kakao ,basic
    private String job; //학생, 취준생, 직장인, 백수..
    private String main_lang; // java, python, c++, c, kotlin..

}
