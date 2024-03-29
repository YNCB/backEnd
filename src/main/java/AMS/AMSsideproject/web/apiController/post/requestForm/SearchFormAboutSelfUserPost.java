package AMS.AMSsideproject.web.apiController.post.requestForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 특정 user 페이지 api는 -> "/codebox/user1" 인데
 * 내 페이지인지 상대방 페이지 인지는 구분해야되나?!
 * 모 구분할필요가 없지 필터링 조건이 약간 다른데 그거는 없으면 null 처리 하면 되니까?!
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchFormAboutSelfUserPost {

    @ApiModelProperty(example = "BFS,DFS", notes = "없을시 빈배열([])로 주시기 바랍니다.")
    private List<String> tags; //tags

    @ApiModelProperty(example = "see", notes = "see(보고푼문제), alone(혼자푼문제), 모든 문제일 경우 null로 채워주시면 됩니다.")
    private String type; //ALL, SEE, ALONE

    @ApiModelProperty(example = "Java", notes = "모든 언어인 경우 null로 채워주시면 됩니다.")
    private String language;

    @ApiModelProperty(example = "test", notes = "게시물 제목 대상, 없을시 null로 채워주시면 됩니다.")
    private String searchTitle; //제목 검색

    @ApiModelProperty(example = "latest(최신순)", notes = "정렬기준 -> latest(최신순)-default, oldest(오래된순), likeNum(좋아요순), replyNum(댓글많은순), countView(조회수순)")
    @NotBlank(message = "필수 입력값 입니다.")
    private String orderKey; // 정렬 기준 -> latest(최신순), oldest(오래된순), likeNum(좋아요순), replyNum(댓글많은순), countView(조회수순)

    @ApiModelProperty(example = "1", notes = "무한스크롤 방식을 위한것, 마지막 게시물의 게시물 고유 PK id(post_id)입니다. " +
            "latest,oldest,likeNum,replyNum,countView 모두 해당합니다. 없을시 null로 채워주시면 됩니다. ")
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
