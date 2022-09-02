package AMS.AMSsideproject.web.apiController.user.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinForm {

    /**
     * validation 적용하기 -> bingResult 쓰기
     */
    private String id; //사용자 id
    private String password;
    private String nickname;
    private int birth; //월,일만
    private String email;

    private String social_type; //google, kakao
    private String job; //학생, 취준생, 직장인, 백수..
    private String main_lang; // java, python, c++, c, kotlin..

}
