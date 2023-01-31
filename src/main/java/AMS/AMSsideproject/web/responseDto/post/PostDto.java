package AMS.AMSsideproject.web.responseDto.post;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.Type;
import AMS.AMSsideproject.domain.post.repository.query.form.LikeDto;
import AMS.AMSsideproject.web.responseDto.reply.RepliesDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostDto {

    @ApiModelProperty(example = "1")
    private Long post_id;

    @ApiModelProperty(example = "test")
    private String title; //제목

    @ApiModelProperty(example = "['DFS','BFS']", notes = "문제 태그 리스트")
    private List<String> tags; //태그들

    @ApiModelProperty(example = "user")
    private String nickname; //작성자 닉네임

    @ApiModelProperty(example = "2020-02-02")
    private String redate; //게시물 등록 일자

    @ApiModelProperty(example = "Java")
    private String language; //푼 언어
    @ApiModelProperty(example = "보고 푼문제")
    private String type; //보고푼문제, 혼자 푼문제
    @ApiModelProperty(example = "4")
    private Integer level;
    @ApiModelProperty(example = "test")
    private String context;

    @ApiModelProperty(example = "10")
    private Integer replyNum; //댓글수
    @ApiModelProperty(example = "5")
    private Integer likeNum; //좋아요수
    @ApiModelProperty(example = "6")
    private Integer countView; //조회수

    @ApiModelProperty(example = "true", notes = "좋아요 누른 유무")
    private boolean likeExisting; //좋아요 누른 유무

    private List<RepliesDto> replies; //댓글 리스트

    public PostDto(Long postId, String title, String nickname, LocalDateTime redate, Integer likeNum, Integer countView, String language, Type type,
                   Integer level, String context, Integer replyNum) {

        this.post_id = postId;
        this.title = title;
        this.nickname = nickname;
        this.redate = redate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        this.likeNum = likeNum;
        this.language = language;
        this.type = type.name();
        this.level = level;
        this.context = context;
        this.replyNum = replyNum;
        this.countView = countView;

        this.likeExisting = false; //초기값
    }
}
