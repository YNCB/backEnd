package AMS.AMSsideproject.domain.reply;

import AMS.AMSsideproject.domain.post.Post;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reply_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    //!!!닉네임 바뀔수 있으니 "user_id" 를 넣어야되는거 아니야?! 찾을땐 join을 통해서 "nickname" 찾아야되는거 아냐?!
    private String nickname;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String context;

    private Integer reGroup;
    private Integer reLevel;

    private LocalDateTime redate;
    private LocalDateTime chdate;


    public void addReplyNumAboutPost() {
        this.post.addReplyNum();
    }

}
