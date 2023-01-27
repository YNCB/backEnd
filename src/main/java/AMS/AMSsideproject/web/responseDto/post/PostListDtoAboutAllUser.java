package AMS.AMSsideproject.web.responseDto.post;

import AMS.AMSsideproject.domain.post.Post;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostListDtoAboutAllUser {

    @ApiModelProperty(example = "2", notes = "게시물 고유 아이디 PK")
    private Long post_id; //게시물 고유 id
    @ApiModelProperty(example = "test")
    private String title; //제목

    /**
     * 사용자 pk 를 줄 이유가 있나?! 사용되지 않을건데..아마
     */
//    @ApiModelProperty(example = "3" , notes = "작성자 고유 아이디")
//    private Long user_id; //작성자 고유 id
    @ApiModelProperty(example = "user")
    private String nickname; //작성자 닉네임 -> fetch join
    @ApiModelProperty(example = "Java")
    private String language; //푼 언어
    @ApiModelProperty(example = "보고 푼문제")
    private String type; //see, alone

    @ApiModelProperty(example = "5")
    private Integer likeNum; //댓글수
    @ApiModelProperty(example = "10")
    private Integer replyNum; //좋아요수
    @ApiModelProperty(example = "6")
    private Integer countView; //조회수

    @ApiModelProperty(example = "DFS", notes = "문제 태그들")
    private List<String> tags; //태그들

    public PostListDtoAboutAllUser(Post post) {
        post_id = post.getPost_id();
        title = post.getTitle();
        //user_id = post.getUser().getUser_id(); //"fetchJoin" 으로 쿼리문 발생 x
        nickname = post.getUser().getNickname(); //"fetchJoin" 으로 쿼리문 발생 x
        language = post.getLanguage();
        type = post.getType().name();

        likeNum = post.getLikeNum();
        //likeNum = post.getLikes().size(); //양방향시 사용
        countView = post.getCountView();
        replyNum = post.getReplyNum();

        tags = post.getPostTagList().stream()
                .map( postTag -> new String(postTag.getTag().getName())) //프록시 초기화 -> "1+N" 문제 "betch" 로 해결 (1:N관계)
                .collect(Collectors.toList());
    }
}
