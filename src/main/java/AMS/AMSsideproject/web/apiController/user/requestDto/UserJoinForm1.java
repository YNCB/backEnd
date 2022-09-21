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
public class UserJoinForm1 {

    @ApiModelProperty(example = "test@gmail.com")
    @NotBlank(message = "필수 입력값 입니다.")
    @Email(message = "이메일 형식이 맞지 않습니다.")
    private String email;

    @ApiModelProperty(example = "test1234!")
    @NotBlank(message = "필수 입력값 입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
    message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    private String password;


}
