package AMS.AMSsideproject.web.apiController.user.requestDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RequestEmailAuthDto {

    @ApiModelProperty(example = "test@gamil.com")
    public String email;
}
