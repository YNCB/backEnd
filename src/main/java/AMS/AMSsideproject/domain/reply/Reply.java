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
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
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


//    private Integer reDepth; //댓글:0 , 대댓글:1 , 대대댓글:2 등
//    private Integer reLevel; //한 집합의 댓글, 대댓글 총 개수(댓글:0 부터 시작)
    
}
