package AMS.AMSsideproject.web.apiController.follow.requestForm;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowSaveForm {

    @ApiModelProperty(example = "1", notes = "팔로우할 사용자 Id")
    private Long userId; //팔로우할 유저 아이디
}
