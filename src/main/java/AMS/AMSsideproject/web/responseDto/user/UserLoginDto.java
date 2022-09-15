package AMS.AMSsideproject.web.responseDto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

    @ApiModelProperty(example = "1")
    private Long user_Id;
    @ApiModelProperty(example = "test")
    private String nickname;
    @ApiModelProperty(example = "xxxxxx")
    private String accessToken;
    @ApiModelProperty(example = "xxxxxx")
    private String refreshToken;
}
