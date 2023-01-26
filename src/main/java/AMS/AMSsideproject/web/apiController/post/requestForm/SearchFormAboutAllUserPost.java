package AMS.AMSsideproject.web.apiController.post.requestForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFormAboutAllUserPost {

    @ApiModelProperty(example = "Java", notes = "없을시 null로 채워주시면 됩니다.")
    private String language; //사용 언어

    @ApiModelProperty(example = "test", notes = "게시물 제목 대상, 없을시 null로 채워주시면 됩니다.")
    private String searchTitle; //제목 검색

    @ApiModelProperty(example = "latest(최신순)", notes = "정렬기준 -> latest(최신순)-default, oldest(오래된순), likeNum(좋아요순), replyNum(댓글많은순), countView(조회수순)")
    @NotBlank(message = "필수 입력값 입니다.")
    private String orderKey; // 정렬 기준 -> latest(최신순), oldest(오래된순), likeNum(좋아요순), replyNum(댓글많은순), countView(조회수순)

    @ApiModelProperty(example = "1", notes = "무한스크롤 방식을 위한것, 마지막 게시물의 게시물 고유 PK id(post_id)입니다. " +
            "latest,oldest,likeNum,replyNum,countView 모두 해당합니다. 없을시 null로 채워주시면 됩니다.")
    private Long lastPostId; //최신순 사용 , 댓글순 사용, 좋아요순 사용

    @ApiModelProperty(example = "3", notes = "무한스크롤 방식을 위한것, 마지막 게시물의 게시물 댓글수 입니다. " +
            "replyNum(댓글많은순)이 해당합니다. 없을시 null로 채워주시면 됩니다.")
    private Integer lastReplyNum; //댓글순 사용

    @ApiModelProperty(example = "5", notes = "무한스크롤 방식을 위한것, 마지막 게시물의 게시물 좋아요개수 입니다. " +
            "likeNum(좋아요순)이 해당합니다. 없을시 null로 채워주시면 됩니다.")
    private Integer lastLikeNum; //좋아요순 사용

    @ApiModelProperty(example = "4", notes = "무한스크롤 방식을 위한것, 마지막 게시물의 게시물 조회수개수 입니다. " +
            "countView(조회순)이 해당합니다. 없을시 null로 채워주시면 됩니다.")
    private Integer countView; //조회수순 사용
}
