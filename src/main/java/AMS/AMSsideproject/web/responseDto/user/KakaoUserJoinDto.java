package AMS.AMSsideproject.web.responseDto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KakaoUserJoinDto {

    //private String id;
    @ApiModelProperty(example = "test@gamil.com")
    private String email;
    @ApiModelProperty(example = "11222333")
    private String password;
    @ApiModelProperty(example = "본식")
    private String nickname;
    @ApiModelProperty(example = "Google")
    private String social_type;

}
