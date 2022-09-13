package AMS.AMSsideproject.web.responseDto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleUserJoinDto {

    private String email;
    //private String id; //소셜 로그인 고유 아이디
    private String password;
    private String social_type;

}
