package AMS.AMSsideproject.domain.refreshToken;

import AMS.AMSsideproject.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity @Getter @Setter
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refresh_token_id;

    private String refresh_token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //생성자
    public RefreshToken() {}

    public RefreshToken(String refresh_token) {this.refresh_token = refresh_token;}

    //setter
    public void setUser(User user){
        this.user = user;
    }
    public void setRefreshToken(String refreshToken){
        this.refresh_token = refreshToken;
    }

}
