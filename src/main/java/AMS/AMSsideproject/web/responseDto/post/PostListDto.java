package AMS.AMSsideproject.web.responseDto.post;

import AMS.AMSsideproject.domain.post.Post;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostListDto {

    private Long post_id; //게시물 고유 id
    private String title; //제목
    private Long user_id; //작성자 고유 id
    private String nickname; //작성자 닉네임 -> fetch join
    private String language; //푼 언어
    private String type; //보고푼문제, 혼자 푼문제

    private Long likeNum; //댓글수
    private Long replyNum; //좋아요수

    private List<String> tags; //태그들

    public PostListDto(Post post) {
        post_id = post.getPost_id();
        title = post.getTitle();
        user_id = post.getUser().getUser_id(); //"fetchJoin" 으로 쿼리문 발생 x
        nickname = post.getUser().getNickname(); //"fetchJoin" 으로 쿼리문 발생 x
        language = post.getLanguage();
        type = post.getType();
        likeNum = post.getLikeNum();
        replyNum = post.getReplyNum();

        tags = post.getPostTagList().stream()
                .map( postTag -> new String(postTag.getTag().getName())) //프록시 초기화 -> "1+N" 문제 "betch" 로 해결 (1:N관계)
                .collect(Collectors.toList());
    }
}
