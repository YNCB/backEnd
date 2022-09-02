package AMS.AMSsideproject.web.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {

    private Long user_id;
    private String id;
    private String nickname;
}
