package AMS.AMSsideproject.domain.like;

import AMS.AMSsideproject.domain.post.Post;
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

    private Long user_id; //좋아요 누른 사용자 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private LocalDateTime redate; //좋아요 누른 시간






    public void setPost(Post post) {
        this.post = post;
    }

    public static Like create(Post post, Long userId) {
        Like like = new Like();
        like.post = post;
        like.user_id = userId;
        like.redate = LocalDateTime.now();
        return like;
    }
}
