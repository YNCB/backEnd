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
    @NotBlank(message = "필수 입력값 입니다.")
    private String email;

    @ApiModelProperty(example = "test1234!")
    @NotBlank(message = "필수 입력값 입니다.")
    private String password;

    @NotBlank(message = "필수 입력값 입니다.")
    @Length(min=3 , max=10 , message = "3자 ~ 10자 이어야 합니다.")
    @ApiModelProperty(example = "test")
    private String nickname;

    @NotBlank (message = "필수 입력값 입니다.")
    @Pattern(regexp = "KAKAO|GOOGLE|BASIC",
            message = "KAKAO,GOOGLE,BASIC 중 하나입니다.")
    @ApiModelProperty(example = "BASIC", notes = "GOOGLE, KAKAO, BASIC" )
    private String social_type; //GOOGLE,KAKAO,BASIC

    @NotBlank (message = "필수 입력값 입니다.")
    @Pattern(regexp = "학생|취준생|회사원|기타",
            message = "학생|취준생|회사원|기타 중 하나입니다.")
    @ApiModelProperty(example = "학생")
    private String job;

    @NotBlank (message = "필수 입력값 입니다.")
    @Pattern(regexp = "C|C++|C#|Python|JS|Java|Swift|Kotlin|Ruby|Swift|Go|Etc",
            message = "C++/Python/JS/Java/C/C#/Swift/Kotlin/Ruby/Swift/Go/Etc 중 하나입니다.")
    @ApiModelProperty(example = "Java")
    private String main_lang;

}
