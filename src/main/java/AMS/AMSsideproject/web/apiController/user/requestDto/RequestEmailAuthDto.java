package AMS.AMSsideproject.web.apiController.user.requestDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class RequestEmailAuthDto {

    @ApiModelProperty(example = "test@gamil.com")
    @Email
    public String email;
}
