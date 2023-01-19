package AMS.AMSsideproject.domain.user;

import AMS.AMSsideproject.domain.refreshToken.RefreshToken;
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
    private String nickname; //Unique!!

    private String social_type; //google, kakao, etc
    private LocalDateTime redate; //회원가입 일자

    private String job; //학생, 취준생, 직장인, 백수..
    private String main_lang; // java, python, c++, c, kotlin..

    private String role; //USER, MANAGER, ADMIN

    //리프레시 토큰
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "refresh_token_id")
    private RefreshToken refreshToken;


    //생성 메서드
    public static User createUser(UserJoinForm2 joinForm) {
        User user = new User();
        user.setEmail(joinForm.getEmail());
        user.setPassword(joinForm.getPassword());
        user.setNickname(joinForm.getNickname());

        user.setSocial_type(joinForm.getSocial_type());
        user.setJob(joinForm.getJob());
        user.setMain_lang(joinForm.getMain_lang());
        user.setRedate(LocalDateTime.now());

        /**
         * 임시
         */
        user.setRole("USER");

        return user;
    }

    public void update(UserEditForm userEditForm) {
        this.nickname = userEditForm.nickName;
        this.job = userEditForm.job;
        this.main_lang = userEditForm.main_lang;
    }

    //setter 메서드
    public RefreshToken setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
        return this.refreshToken;
    }



}
