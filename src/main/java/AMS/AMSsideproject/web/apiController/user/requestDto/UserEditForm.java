package AMS.AMSsideproject.web.apiController.user.requestDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditForm {

    @ApiModelProperty(example = "test")
    public String nickName;
    @ApiModelProperty(example = "Worker")
    public String job;
    @ApiModelProperty(example = "Python")
    public String main_lang;

}