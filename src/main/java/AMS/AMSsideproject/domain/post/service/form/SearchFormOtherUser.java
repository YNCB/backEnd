package AMS.AMSsideproject.domain.post.service.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchFormOtherUser {

    private String language;
    private String searchTitle; //제목 검색

    private String orderKey; // 정렬 기준 -> latest(최신순), oldest(오래된순), likeNum(좋아요순), replyNum(댓글많은순)
    private Long lastPostId; //최신순 사용 , 댓글순 사용, 좋아요순 사용
    private Integer lastReplyNum; //댓글순 사용
    private Integer lastLikeNum; //좋아요순 사용
}
