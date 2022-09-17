package AMS.AMSsideproject.web.responseDto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditSuccessDto {

    @ApiModelProperty(example = "1")
    private Long user_id;
    @ApiModelProperty(example = "codebox")
    private String nickname;
    @ApiModelProperty(example = "xxxx")
    private String accessToken;
    @ApiModelProperty(example = "xxxx")
    private String refreshToken;
}
