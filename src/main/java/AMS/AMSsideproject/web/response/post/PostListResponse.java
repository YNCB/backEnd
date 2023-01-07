package AMS.AMSsideproject.web.response.post;

import AMS.AMSsideproject.web.responseDto.post.PostListDtoAboutAllUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PostListResponse<T> {

    private T list;

    @ApiModelProperty(example = "1" , notes = "응답되는 게시물 개수")
    private Integer count; //응답 게시물 리스트 개수

    @ApiModelProperty(example = "true", notes = "무한스크롤 방식을 위해 사용. 다음 게시물 존재 유무")
    private Boolean hasNext; //다음 게시물이 있는지 유무

    public PostListResponse(T list, Integer count, Boolean hasNext) {
        this.list = list;
        this.count = count;
        this.hasNext = hasNext;
    }
}
