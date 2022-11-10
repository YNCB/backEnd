package AMS.AMSsideproject.web.responseDto.post;

import AMS.AMSsideproject.domain.post.Post;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostListDtoAboutSpecificUser {

    @ApiModelProperty(example = "2", notes = "게시물 고유 아이디 PK")
    private Long post_id; //게시물 고유 id
    @ApiModelProperty(example = "test")
    private String title; //제목

    @ApiModelProperty(example = "Java")
    private String language; //푼 언어
    @ApiModelProperty(example = "보고 푼문제")
    private String type; //보고푼문제, 혼자 푼문제

    @ApiModelProperty(example = "5")
    private Integer likeNum; //댓글수
    @ApiModelProperty(example = "10")
    private Integer replyNum; //좋아요수

    @ApiModelProperty(example = "DFS", notes = "문제 태그들")
    private List<String> tags; //태그들

    public PostListDtoAboutSpecificUser(Post post) {
        post_id = post.getPost_id();
        title = post.getTitle();
        language = post.getLanguage();
        type = post.getType();

        //likeNum = post.getLikeNum();
        likeNum = post.getLikes().size();

        replyNum = post.getReplyNum();

        tags = post.getPostTagList().stream()
                .map( postTag -> new String(postTag.getTag().getName())) //프록시 초기화 -> "1+N" 문제 "betch" 로 해결 (1:N관계)
                .collect(Collectors.toList());
    }
}
