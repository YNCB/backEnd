package AMS.AMSsideproject.web.responseDto.user;

import AMS.AMSsideproject.web.jwt.JwtProperties;
import AMS.AMSsideproject.web.jwt.JwtToken;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class UserTokenDto {

    @ApiModelProperty(example = "xxxxxx")
    private String Authorization;
    @ApiModelProperty(example = "xxxxxx")
    private String RefreshToken;
    @ApiModelProperty(example = "2021-09-02T14:56:20.699")
    private String expireTime;

    public UserTokenDto(JwtToken jwtToken) {

        this.Authorization = jwtToken.getAuthorization();
        this.RefreshToken = jwtToken.getRefreshToken();

        LocalDateTime localDateTime = LocalDateTime.now().plusSeconds(JwtProperties.ACCESSTOKEN_TIME / 1000);
        this.expireTime = localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
