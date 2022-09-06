package AMS.AMSsideproject.web.apiController.user.form;

import AMS.AMSsideproject.domain.user.Birthday;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
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

    private Integer year;
    private Integer month;
    private Integer day;

    private String email;
    private String social_type; //google, kakao ,basic
    private String job; //학생, 취준생, 직장인, 백수..
    private String main_lang; // java, python, c++, c, kotlin..

}
