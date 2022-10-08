package AMS.AMSsideproject.domain.tag.postTag;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.tag.Tag.Tag;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long post_tag_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public void setPost(Post post) {
        this.post = post;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public static PostTag createPostTag(Tag tag) {
        PostTag postTag = new PostTag();
        postTag.tag = tag;
        return postTag;
    }
}
