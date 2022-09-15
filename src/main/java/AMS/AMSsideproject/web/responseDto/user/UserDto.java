package AMS.AMSsideproject.web.responseDto.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @ApiModelProperty(example = "1")
    private Long user_id;
    @ApiModelProperty(example = "codebox")
    private String nickname;
}
