package AMS.AMSsideproject.web.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginDto {

    private Long user_Id;
    private String nickname;
    private String accessToken;
    private String refreshToken;
}
