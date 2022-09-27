package AMS.AMSsideproject.domain.post.repository.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchFormAboutAllUser {

    private String language; //사용 언어
    private String searchTitle; //제목 검색
    private String orderKey; // 정렬 기준 -> redate(최신순), likeNum(좋아요순), replyNum(댓글많은순)

    private Long lastPostId; //최신순 사용 , 댓글순 사용, 좋아요순 사용
    private Long lastReplyNum; //댓글순 사용
    private Long lastLikeNum; //좋아요순 사용
}
