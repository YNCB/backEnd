package AMS.AMSsideproject.web.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserJoinDto {

    private String id;
    private String password;
    private String nickname;
    private String birth;
    private String email;
    private String social_type;

}
