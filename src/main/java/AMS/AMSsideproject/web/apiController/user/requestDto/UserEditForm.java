package AMS.AMSsideproject.web.apiController.user.requestDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditForm {

    @ApiModelProperty(example = "test")
    @NotBlank(message = "필수 입력값 입니다.")
    @Length(min=3 , max=10 , message = "3자 ~ 10자 이어야 합니다.")
    public String nickName;

    @ApiModelProperty(example = "학생")
    @NotBlank(message = "필수 입력값 입니다.")
    public String job;

    @ApiModelProperty(example = "Python")
    @NotBlank(message = "필수 입력값 입니다.")
    public String main_lang;

}
