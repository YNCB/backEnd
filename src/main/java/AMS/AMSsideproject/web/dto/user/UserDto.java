package AMS.AMSsideproject.web.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private Long user_id;
    private String social_id;
    private String nickname;
}
