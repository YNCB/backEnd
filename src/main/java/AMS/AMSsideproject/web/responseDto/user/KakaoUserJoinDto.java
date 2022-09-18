package AMS.AMSsideproject.web.responseDto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class KakaoUserJoinDto {

    @ApiModelProperty(example = "test@gamil.com")
    private String email;
    @ApiModelProperty(example = "kakao1234!", notes = "kakao 회원가입은 예시와 같이 패스워드 고정")
    private String password;
    @ApiModelProperty(example = "본식", notes = "kakao 회원가입은 기본 kakao 닉네임 받을수 있음")
    private String nickname;
    @ApiModelProperty(example = "Kakao")
    private String social_type;

}
