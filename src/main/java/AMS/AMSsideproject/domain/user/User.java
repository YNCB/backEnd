package AMS.AMSsideproject.domain.user;

import AMS.AMSsideproject.domain.token.RefreshToken;
import AMS.AMSsideproject.web.apiController.user.form.UserJoinForm;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    private String social_id; //소설 플랫폼 고유 id
    //private String password; //꼭 필요하나?!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private String nickname;
    private int birth; //월,일만
    private String email;

    private String social_type; //google, kakao
    private LocalDateTime redate; //회원가입 일자

    private String job; //학생, 취준생, 직장인, 백수..
    private String main_lang; // java, python, c++, c, kotlin..

    private String role; //사용자 권한 (Reader, Master)

    //리프레시 토큰
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "refresh_token_id")
    private RefreshToken refreshToken;


    //생성 메서드
    public static User createUser(UserJoinForm joinForm) {
        User user = new User();
        user.setSocial_id(joinForm.getSocial_id());
        user.setNickname(joinForm.getNickname());
        user.setBirth(joinForm.getBirth());
        user.setEmail(joinForm.getEmail());
        user.setSocial_type(joinForm.getSocial_type());
        user.setJob(joinForm.getJob());
        user.setMain_lang(joinForm.getMain_lang());
        user.setRedate(LocalDateTime.now());

        /**
         * 임시
         */
        user.setRole("MASTER");

        return user;
    }


    //setter 메서드
    public RefreshToken setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
        return this.refreshToken;
    }



}
