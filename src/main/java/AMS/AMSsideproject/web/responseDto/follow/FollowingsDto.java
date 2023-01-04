package AMS.AMSsideproject.web.responseDto.follow;

import AMS.AMSsideproject.domain.follow.Follow;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FollowingsDto {

    @ApiModelProperty(example = "1", notes = "나를 팔로우한 사용자 Id")
    private Long userId;
    @ApiModelProperty(example = "1", notes = "나를 팔로우한 사용자 닉네임")
    private String nickname;

    public FollowingsDto(Follow follow) {
        this.userId = follow.getUser().getUser_id();
        this.nickname = follow.getUser().getNickname();
    }

}
