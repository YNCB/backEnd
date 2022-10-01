package AMS.AMSsideproject.domain.post.repository.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 특정 user 페이지 api는 -> "/codebox/user1" 인데
 * 내 페이지인지 상대방 페이지 인지는 구분해야되나?!
 * 모 구분할필요가 없지 필터링 조건이 약간 다른데 그거는 없으면 null 처리 하면 되니까?!
 */
@Data
@Builder
public class SearchFormAboutSpecificUser {

    /**
     * 내 페이지만 있는것
     */
    private List<String> tags; //tags
    private String type; //ALL, SEE, ALONE

    /**
     * 다른유저 페이지만 있는것
     */
    private String language;

    /**
     * 공통
     */
    private String searchTitle; //제목 검색
    private String orderKey; // 정렬 기준 -> redate(최신순), likeNum(좋아요순), replyNum(댓글많은순)
    private Long lastPostId; //최신순 사용 , 댓글순 사용, 좋아요순 사용
    private Long lastReplyNum; //댓글순 사용
    private Long lastLikeNum; //좋아요순 사용
}
