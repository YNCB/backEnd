package AMS.AMSsideproject.web.responseDto.user;

import AMS.AMSsideproject.web.jwt.JwtProperties;
import AMS.AMSsideproject.web.jwt.JwtToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

    @ApiModelProperty(example = "1")
    private Long userId;

    @ApiModelProperty(example = "test")
    private String nickname;

    @ApiModelProperty(example = "xxxxxx")
    private String Authorization;

    @ApiModelProperty(example = "xxxxxx")
    private String RefreshToken;

    @ApiModelProperty(example = "2021-09-02T14:56:20.699")
    private String expireTime;

    public UserLoginDto(Long userId, String nickname, JwtToken jwtToken) {
        this.userId = userId;
        this.nickname = nickname;
        this.Authorization = jwtToken.getAuthorization();
        this.RefreshToken = jwtToken.getRefreshToken();

        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(JwtProperties.ACCESSTOKEN_TIME / 1000);
        this.expireTime = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
