package AMS.AMSsideproject.web.apiController.user.requestDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class RequestValidNickName {

    @ApiModelProperty(example = "test")
    public String nickName;
}
