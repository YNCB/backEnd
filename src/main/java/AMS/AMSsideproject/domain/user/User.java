package AMS.AMSsideproject.domain.user;

import AMS.AMSsideproject.web.apiController.user.requestDto.UserEditForm;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserJoinForm2;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    private String email;
    private String password;
    private String nickname;

    @Enumerated(value = EnumType.STRING)
    private LoginType type;
    //private String social_type; //google, kakao, etc

    private LocalDateTime redate; //회원가입 일자

    @Enumerated(value = EnumType.STRING)
    private Job job; //학생, 취준생, 직장인, 기타
    //private String job;

    private String main_lang;

    private Role role; //USER, MANAGER, ADMIN

    //생성 메서드
    public static User createUser(UserJoinForm2 joinForm) {
        User user = new User();
        user.email = joinForm.getEmail();
        user.password = joinForm.getPassword();
        user.nickname = joinForm.getNickname();
        user.type = LoginType.valueOf(joinForm.getSocial_type());
        user.job = Job.valueOf(joinForm.getJob());
        user.main_lang = joinForm.getMain_lang();
        user.redate = LocalDateTime.now();

        user.role = Role.USER;
        return user;
    }

    public void update(UserEditForm userEditForm) {
        this.nickname = userEditForm.nickName;
        this.job = Job.valueOf(userEditForm.job);
        this.main_lang = userEditForm.main_lang;
    }


}
