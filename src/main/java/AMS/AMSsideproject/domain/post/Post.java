package AMS.AMSsideproject.domain.post;

import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.web.apiController.post.requestDto.PostSaveForm;
import org.springframework.cglib.core.Local;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String nickname; //작성자 닉네임
    private String title;
    private String problem_uri;
    @Column(columnDefinition = "TEXT")
    private String context;

    private String type; //문제 유형(다시풀문제, 다푼문제)
    private String language; //문제 푼 언어(JAVA, Python etc)

    private LocalDateTime redate;
    private LocalDateTime chdate;

    @Column(name = "like_num")
    private Long likeNum; //좋아요 개수
    @Column(name = "reply_num")
    private Long replyNum; //댓글 개수
    private Integer level; //난의도


    //생성 메서드
    public static Post createPost(User user, PostSaveForm postSaveForm) {
        Post post = new Post();

        post.user = user;
        post.title = postSaveForm.getTitle();
        post.problem_uri = postSaveForm.getProblem_uri();
        post.context = postSaveForm.getContent();

        post.type = postSaveForm.getType();
        post.language = postSaveForm.getLanguage();
        post.level = postSaveForm.getLevel();

        post.redate = LocalDateTime.now();

        return post;
    }

}
