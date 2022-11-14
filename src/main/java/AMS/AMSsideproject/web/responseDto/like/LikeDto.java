package AMS.AMSsideproject.web.responseDto.like;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeDto {

    @ApiModelProperty(example = "true", notes = "true:좋아요 활성화, false: 좋아요 비활성화")
    private boolean existing;
    @ApiModelProperty(example = "4", notes = "게시물 좋아요 개수")
    private Integer num;

}
