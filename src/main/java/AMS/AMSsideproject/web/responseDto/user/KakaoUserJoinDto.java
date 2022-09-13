package AMS.AMSsideproject.web.responseDto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KakaoUserJoinDto {

    //private String id;
    private String email;
    private String password;
    private String nickname;
    private String social_type;

}
