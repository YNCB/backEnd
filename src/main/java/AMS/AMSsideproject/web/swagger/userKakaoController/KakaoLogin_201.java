package AMS.AMSsideproject.web.swagger.userKakaoController;

import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.user.GoogleUserJoinDto;
import AMS.AMSsideproject.web.responseDto.user.KakaoUserJoinDto;

public class KakaoLogin_201 extends DataResponse<KakaoUserJoinDto> {
    public KakaoLogin_201(String status, String message, KakaoUserJoinDto data) {
        super(status, message, data);
    }
}
