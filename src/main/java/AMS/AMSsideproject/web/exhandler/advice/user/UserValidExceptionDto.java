package AMS.AMSsideproject.web.exhandler.advice.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserValidExceptionDto {

    @ApiModelProperty(example = "nickname")
    private String key;
    @ApiModelProperty(example = "te")
    private String value;
    @ApiModelProperty(example = "3자 ~ 10자 이어야 합니다.")
    private String message;
}
