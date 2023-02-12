package AMS.AMSsideproject.web.response.post;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserPagePostListDto<T> {

    private T list;

    @ApiModelProperty(example = "1" , notes = "응답되는 게시물 개수")
    private Integer count; //응답 게시물 리스트 개수

    @ApiModelProperty(example = "true", notes = "무한스크롤 방식을 위해 사용. 다음 게시물 존재 유무")
    private Boolean hasNext; //다음 게시물이 있는지 유무

    @ApiModelProperty(example = "1" , notes = "특정 페이지 사용자 고유 id")
    private Long userId;

    @ApiModelProperty(example = "false" , notes = "팔로우 유무")
    private Boolean isFollow;

    //생성자
    public UserPagePostListDto(T list, Integer count, Boolean hasNext, Long userId, Boolean isFollow) {
        this.list = list;
        this.count = count;
        this.hasNext = hasNext;
        this.userId = userId;
        this.isFollow = isFollow;
    }
}
