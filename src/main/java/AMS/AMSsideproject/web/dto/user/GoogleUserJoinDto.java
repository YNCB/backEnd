package AMS.AMSsideproject.web.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleUserJoinDto {

    private String id;
    private String password;
    //private String birth; //존재 하지 않음
    private String email;
    private String social_type;

}
