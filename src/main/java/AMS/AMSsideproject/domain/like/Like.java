package AMS.AMSsideproject.domain.like;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "heart")
@Getter
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long like_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; //좋아요 누른 사용자 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private LocalDateTime redate; //좋아요 누른 시간


    public static Like create(Post post, User user) {
        Like like = new Like();
        like.post = post;
        like.user = user;
        like.redate = LocalDateTime.now();
        return like;
    }
}
