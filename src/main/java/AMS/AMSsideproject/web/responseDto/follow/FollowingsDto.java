package AMS.AMSsideproject.web.responseDto.follow;

import AMS.AMSsideproject.domain.follow.Follow;
import lombok.Data;

@Data
public class FollowingsDto {

    private Long userId;
    private String nickname;

    public FollowingsDto(Follow follow) {
        this.userId = follow.getUser().getUser_id();
        this.nickname = follow.getUser().getNickname();
    }

}
