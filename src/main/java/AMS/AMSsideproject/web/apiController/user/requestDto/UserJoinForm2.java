package AMS.AMSsideproject.web.apiController.user.requestDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinForm2 {

    @ApiModelProperty(example = "test@gmail.com")
    private String email;

    @ApiModelProperty(example = "test1234!")
    private String password;

    @NotBlank(message = "필수 입력값 입니다.")
    @Length(min=3 , max=10 , message = "3자 ~ 10자 이어야 합니다.")
    @ApiModelProperty(example = "test")
    private String nickname;

    @NotBlank (message = "필수 입력값 입니다.")
    @ApiModelProperty(example = "google")
    private String social_type; //google, kakao ,basic

    @NotBlank (message = "필수 입력값 입니다.")
    @ApiModelProperty(example = "학생")
    private String job; //학생, 취준생, 직장인, 백수..

    @NotBlank (message = "필수 입력값 입니다.")
    @ApiModelProperty(example = "Java")
    private String main_lang; // java, python, c++, c, kotlin..

}
