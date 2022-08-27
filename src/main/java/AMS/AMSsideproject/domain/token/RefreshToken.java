package AMS.AMSsideproject.domain.token;

import lombok.Getter;

import javax.persistence.*;

@Entity @Getter
@Table(name = "refresh_token")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refresh_token_id;
    private String refresh_token;


    public RefreshToken() {}

    public RefreshToken(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
