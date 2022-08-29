package AMS.AMSsideproject.web.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginDto {

    private String social_id;
    private String nickname;
    private String accessToken;
    private String refreshToken;
}
