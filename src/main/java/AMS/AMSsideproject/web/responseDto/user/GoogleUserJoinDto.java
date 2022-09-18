package AMS.AMSsideproject.web.responseDto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleUserJoinDto {

    @ApiModelProperty(example = "test@gamil.com")
    private String email;
    @ApiModelProperty(example = "google1234!",  notes = "google 회원가입은 예시와 같이 패스워드 고정")
    private String password;
    @ApiModelProperty(example = "Google")
    private String social_type;

}
