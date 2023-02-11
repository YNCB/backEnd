package AMS.AMSsideproject.domain.post;

import AMS.AMSsideproject.domain.like.Like;
import AMS.AMSsideproject.domain.reply.Reply;
import AMS.AMSsideproject.domain.tag.postTag.PostTag;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.web.apiController.post.requestForm.PostEditForm;
import AMS.AMSsideproject.web.apiController.post.requestForm.PostSaveForm;
import io.swagger.models.auth.In;
import lombok.Getter;

import javax.persistence.*;
import java.sql.Array;
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

    @Enumerated(value = EnumType.STRING)
    private Type type;
    //private String type;

    private String language;

    private LocalDateTime redate;
    private LocalDateTime chdate;
    private Integer level; //난이도

    //양방향 연관관계
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true )
    private List<PostTag> postTagList = new ArrayList<>();

    //양방향 연관관계 -> 삭제할때 한번에 자동 삭제를 위해서 사용
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    //양방향 연관관계 -> 삭제할때 한번에 자동 삭제를 위해서 사용
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "post",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Reply> replies = new ArrayList<>();


    @Column(name = "like_num")
    private Integer likeNum; //좋아요 총 개수 -> 정렬에 컬럼 조건으로 사용되기 때문에

    @Column(name = "reply_num")
    private Integer replyNum; //댓글 개수

    @Column(name = "count_view")
    private Integer countView; //조회수

    //양방향 연관관계 메서드
    public void addPostTag(PostTag postTag) {
        postTag.setPost(this);
        postTagList.add(postTag);
    }
    public void addReply(Reply reply){
        replies.add(reply);
    }
    public void addLike(Like like){
        likes.add(like);
    }

    //like 와 양방향 관계 사용됨. (like_v1)
//    public void addLike(Like like){likes.add(like);}
//    public void setLikeNum(){this.likeNum = this.likes.size();}

    //좋아요 관련
    public void addLikeNum() {this.likeNum++;}
    public void subLikeNum() {this.likeNum--;}

    //게시물 조회시 조회 수 늘림 -> "지연 감지" 사용시
//    public void addCountView() {
//        this.countView++;
//    }

    //댓글수 관련
    public void addReplyNum() { this.replyNum++; }
    public void subReplyNum() { this.replyNum--; };


    //생성 메서드
    public static Post createPost(User user, PostSaveForm postSaveForm) {
        Post post = new Post();

        post.user = user;
        post.title = postSaveForm.getTitle();
        post.problem_uri = postSaveForm.getProblem_uri();
        post.context = postSaveForm.getContent();
        post.type = Type.valueOf(postSaveForm.getType());
        post.language = postSaveForm.getLanguage();
        post.level = postSaveForm.getLevel();
        post.likeNum =0;
        post.replyNum =0;
        post.countView = 0; //좋아요 조회수 초기화!!!!!!!!

        post.redate = LocalDateTime.now();
        return post;
    }

    //setter (게시물 업데이트시 사용) - 더티채킹
    public void setPost(PostEditForm postEditForm) {
        this.title = postEditForm.getTitle();
        this.problem_uri = postEditForm.getProblem_uri();
        this.context = postEditForm.getContent();
        this.type = Type.valueOf(postEditForm.getType());
        this.language = postEditForm.getLanguage();
        this.level = postEditForm.getLevel();
        this.chdate = LocalDateTime.now();
    }

}
