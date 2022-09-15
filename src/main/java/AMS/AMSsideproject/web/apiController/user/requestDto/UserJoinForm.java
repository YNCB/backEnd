package AMS.AMSsideproject.web.apiController.user.requestDto;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinForm {

    /**
     * validation 적용하기 -> bingResult 쓰기
     */
    @ApiModelProperty(example = "test@gmail.com")
    private String email;
    @ApiModelProperty(example = "test")
    private String password;
    @ApiModelProperty(example = "test")
    private String nickname;
    @ApiModelProperty(example = "google")
    private String social_type; //google, kakao ,basic
    @ApiModelProperty(example = "학생")
    private String job; //학생, 취준생, 직장인, 백수..
    @ApiModelProperty(example = "Java")
    private String main_lang; // java, python, c++, c, kotlin..

}
