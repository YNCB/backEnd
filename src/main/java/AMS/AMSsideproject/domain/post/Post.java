package AMS.AMSsideproject.domain.post;

import AMS.AMSsideproject.domain.tag.postTag.PostTag;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.web.apiController.post.requestDto.PostSaveForm;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String problem_uri;
    @Column(columnDefinition = "TEXT")
    private String context;

    private String type; //문제 유형(다시풀문제, 다푼문제)
    private String language; //문제 푼 언어(Java, Python 등)

    private LocalDateTime redate;
    private LocalDateTime chdate;

    @Column(name = "like_num")
    private Long likeNum; //좋아요 개수

    @Column(name = "reply_num")
    private Long replyNum; //댓글 개수

    private Integer level; //난의도

    //양방향 연관관계
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostTag> postTagList = new ArrayList<>();

    public void addPostTag(PostTag postTag) {
        postTag.setPost(this);
        postTagList.add(postTag);
    }


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

        post.likeNum =0L;
        post.replyNum =0L;
        post.redate = LocalDateTime.now();
        return post;
    }

    public void addReplyNum() {
        this.replyNum++;
    }

    public void addLikeNum(Long num) {
        this.likeNum += num;
    }

}
