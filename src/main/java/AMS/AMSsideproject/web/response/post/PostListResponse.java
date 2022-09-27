package AMS.AMSsideproject.web.response.post;

import AMS.AMSsideproject.web.responseDto.post.PostListDto;
import lombok.Data;

import java.util.List;

@Data
public class PostListResponse {

    private List<PostListDto> data;
    private Integer count; //응답 게시물 리스트 개수
    private Boolean hasNext; //다음 게시물이 있는지 유무

    public PostListResponse(List<PostListDto> data, Integer count, Boolean hasNext) {
        this.data = data;
        this.count = count;
        this.hasNext = hasNext;
    }
}
