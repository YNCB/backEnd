package AMS.AMSsideproject.web.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserJoinDto {

    private String social_id;
    private String nickname;
    private String birth;
    private String email;
    private String social_type;

}
