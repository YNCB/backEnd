package AMS.AMSsideproject.web.responseDto.user;

import AMS.AMSsideproject.web.auth.jwt.JwtProperties;
import AMS.AMSsideproject.web.auth.jwt.JwtToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEditSuccessDto {

    @ApiModelProperty(example = "1")
    private Long userId;
    @ApiModelProperty(example = "codebox")
    private String nickname;

    @ApiModelProperty(example = "xxxx")
    private String Authorization;
    @ApiModelProperty(example = "xxxx")
    private String RefreshToken;
//    @ApiModelProperty(example = "xxxx")
//    private String my_session;
    @ApiModelProperty(example = "2021-09-02T14:56:20.699")
    private String expireTime;

    public UserEditSuccessDto(Long userId, String nickname, JwtToken jwtToken) {
        this.userId = userId;
        this.nickname = nickname;
        this.Authorization = jwtToken.getAuthorization();
        this.RefreshToken = jwtToken.getRefreshToken();
       // this.my_session = jwtToken.getMy_session();

        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(JwtProperties.ACCESSTOKEN_TIME / 1000);
        this.expireTime = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
