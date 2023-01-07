package AMS.AMSsideproject.web.responseDto.user;

import AMS.AMSsideproject.domain.user.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserEditDto {

    @ApiModelProperty(example = "test")
    private String nickName;
    @ApiModelProperty(example = "학생")
    private String job;
    @ApiModelProperty(example = "Java")
    private String main_lang;

    public static UserEditDto createUserEditDto(User user) {

        UserEditDto userEditDto =new UserEditDto();
        userEditDto.nickName = user.getNickname();
        userEditDto.job = user.getJob();
        userEditDto.main_lang = user.getMain_lang();
        return userEditDto;
    }

}
