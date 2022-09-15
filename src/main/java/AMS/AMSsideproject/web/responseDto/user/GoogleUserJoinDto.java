package AMS.AMSsideproject.web.responseDto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleUserJoinDto {

    @ApiModelProperty(example = "test@gamil.com")
    private String email;
    //private String id; //소셜 로그인 고유 아이디
    @ApiModelProperty(example = "11222333")
    private String password;
    @ApiModelProperty(example = "Google")
    private String social_type;

}
