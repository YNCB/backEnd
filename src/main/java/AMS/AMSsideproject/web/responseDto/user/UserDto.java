package AMS.AMSsideproject.web.responseDto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private Long user_id;
    private String email;
    private String nickname;
}
