package AMS.AMSsideproject.web.responseDto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseValidNickName {

    @ApiModelProperty(example = "test")
    public String nickName;
}
