package AMS.AMSsideproject.web.responseDto.post;

import AMS.AMSsideproject.domain.post.repository.query.form.LikeDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginPostDto {

    @ApiModelProperty(example = "test")
    private String title; //제목

    @ApiModelProperty(example = "user")
    private String nickname; //작성자 닉네임

    @ApiModelProperty(example = "2020-2-2")
    private String redate; //게시물 등록 일자
    @ApiModelProperty(example = "5")
    private Integer likeNum; //좋아요수
    @ApiModelProperty(example = "true")
    private boolean likeExisting;

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

    @ApiModelProperty(example = "DFS", notes = "문제 태그들")
    private List<String> tags; //태그들

    //!!!!!!!!!!!!!!!!!!!
    private List<LikeDto> likes; //좋아요들


    public static LoginPostDto create(PostDto postDto, boolean existing) {
        LoginPostDto loginPostDto = new LoginPostDto();
        loginPostDto.title = postDto.getTitle();
        loginPostDto.nickname = postDto.getNickname();
        loginPostDto.redate = postDto.getRedate();
        loginPostDto.likeNum = postDto.getLikeNum();

        loginPostDto.likeExisting = existing;

        loginPostDto.language = postDto.getLanguage();
        loginPostDto.type = postDto.getType();
        loginPostDto.level = postDto.getLevel();
        loginPostDto.context = postDto.getContext();

        loginPostDto.replyNum = postDto.getReplyNum();
        loginPostDto.tags = postDto.getTags();
        loginPostDto.likes = postDto.getLikes();

        return loginPostDto;
    }


}
