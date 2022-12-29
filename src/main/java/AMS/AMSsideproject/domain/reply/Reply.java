package AMS.AMSsideproject.domain.reply;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reply_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; //작성자
    
    private String title;

    //@Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime redate; //작성일
    private LocalDateTime chdate; //수정일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Reply parent; // 부모 댓글

    @OneToMany(
            mappedBy = "parent",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Reply> children = new ArrayList<>(); //자식 댓글


    //생성자
    static public Reply createReply(String title, String content, User user, Post post) {
        Reply reply = new Reply();
        reply.post = post;
        reply.user = user;
        reply.title = title;
        reply.content = content;
        reply.parent = null; //초기값
        reply.redate = LocalDateTime.now();
        reply.chdate = null;
        return reply;
    }

    //양방향 연관관계
    public void setParent(Reply parent){
        this.parent = parent;
        parent.getChildren().add(this);
    }

    //수정자
    public void edit(String title, String content) {
        this.title = title;
        this.content = content;
        this.chdate = LocalDateTime.now(); //수정일 설정
    }

}
