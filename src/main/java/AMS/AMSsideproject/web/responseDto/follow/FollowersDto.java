package AMS.AMSsideproject.web.responseDto.follow;

import AMS.AMSsideproject.domain.follow.Follow;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class FollowersDto {

    @ApiModelProperty(example = "1", notes = "내가 팔로우한 사용자 Id")
    private Long userId;
    @ApiModelProperty(example = "홍길동", notes = "내가 팔로우한 사용자 닉네임")
    private String nickname;

    public FollowersDto(Follow follow) {
        this.userId = follow.getFollow().getUser_id();
        this.nickname = follow.getFollow().getNickname();
    }

}
