package AMS.AMSsideproject.domain.post.repository.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SearchFormAboutSpecificUser {

    private List<String> tags; //tags
    private Type type; //ALL, SEE, ALONE
    private String searchTitle; //제목 검색
    private OrderKey orderKey; // 정렬 기준 -> redate(최신순), likeNum(좋아요순), replyNum(댓글많은순)

    private Long lastPostId; //최신순 사용 , 댓글순 사용, 좋아요순 사용
    private Long lastReplyNum; //댓글순 사용
    private Long lastLikeNum; //좋아요순 사용
}
