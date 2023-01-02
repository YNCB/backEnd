package AMS.AMSsideproject.domain.follow;

import AMS.AMSsideproject.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; //팔로우 신청한 회원

    @ManyToOne
    @JoinColumn(name = "follow_id")
    private User follow; //팔로우 당한 회원

    private LocalDateTime redate; //팔로우 신청한 시간

    //생성자
    static public Follow createFollow(User user, User follow) {
        Follow newFollow = new Follow();
        newFollow.user = user;
        newFollow.follow = follow;
        newFollow.redate = LocalDateTime.now();
        return newFollow;
    }
}
