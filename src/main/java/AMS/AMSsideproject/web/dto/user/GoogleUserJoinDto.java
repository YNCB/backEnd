package AMS.AMSsideproject.web.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleUserJoinDto {

    private String id;
    private String password;
    private String birth;
    private String email;
    private String social_type;

    /**
     * 생년월일은 못받나?!
     */
}
