package AMS.AMSsideproject.web.apiController.post.requestForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostSearchFormAboutAllUser {

    @ApiModelProperty(example = "Java", notes = "C++/Python/JS/Java/C/C#/Swift/Kotlin/Ruby/Swift/Go/Etc. 없을시 공백으로 채워주시면 됩니다.")
    @NotNull(message = "필수 입렵값 입니다.")
    @Pattern(regexp = "(C)|(C\\+\\+)|(C#)|(Python)|(JS)|(Java)|(Swift)|(Kotlin)|(Ruby)|(Swift)|(Go)|(Etc)|^$",
            message = "C++/Python/JS/Java/C/C#/Swift/Kotlin/Ruby/Swift/Go/Etc/공백 중 하나입니다.")
    private String language; //사용 언어

    @ApiModelProperty(example = "test", notes = "게시물 제목, 없을시 공백로 채워주시면 됩니다.")
    @NotNull(message = "필수 입렵값 입니다.")
    private String searchTitle; //제목 검색

    @ApiModelProperty(example = "latest", notes = "정렬기준 -> latest(최신순)-default, oldest(오래된순), likeNum(좋아요순), replyNum(댓글많은순), countView(조회수순)")
    @NotBlank(message = "필수 입력값 입니다.")
    @Pattern(regexp = "latest|oldest|likeNum|replyNum|countView",
            message = "latest,oldest,likeNum,replyNum,countView 중 하나입니다.")
    private String orderKey; // 정렬 기준 -> latest(최신순), oldest(오래된순), likeNum(좋아요순), replyNum(댓글많은순), countView(조회수순)

    @ApiModelProperty(example = "1", notes = "마지막 게시물의 게시물 id 입니다. " +
            "latest,oldest,likeNum,replyNum,countView 모두 해당합니다. 없을시 null로 채워주시면 됩니다.")
    private Long lastPostId; //최신순 사용 , 댓글순 사용, 좋아요순 사용

    @ApiModelProperty(example = "3", notes = "마지막 게시물의 댓글수 입니다. " +
            "replyNum(댓글많은순)이 해당합니다. 없을시 null로 채워주시면 됩니다.")
    private Integer lastReplyNum; //댓글순 사용

    @ApiModelProperty(example = "5", notes = "마지막 게시물의 좋아요개수 입니다. " +
            "likeNum(좋아요순)이 해당합니다. 없을시 null로 채워주시면 됩니다.")
    private Integer lastLikeNum; //좋아요순 사용

    @ApiModelProperty(example = "4", notes = "마지막 게시물의 게시물 조회수 입니다. " +
            "countView(조회순)이 해당합니다. 없을시 null로 채워주시면 됩니다.")
    private Integer countView; //조회수순 사용
}
