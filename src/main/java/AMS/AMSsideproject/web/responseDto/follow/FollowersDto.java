package AMS.AMSsideproject.web.responseDto.follow;

import AMS.AMSsideproject.domain.follow.Follow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class FollowersDto {

    private Long userId;
    private String nickname;

    public FollowersDto(Follow follow) {
        this.userId = follow.getFollow().getUser_id();
        this.nickname = follow.getFollow().getNickname();
    }

}
