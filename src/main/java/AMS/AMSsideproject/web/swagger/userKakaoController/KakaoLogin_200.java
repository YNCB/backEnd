package AMS.AMSsideproject.web.swagger.userKakaoController;

import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.user.UserLoginDto;

public class KakaoLogin_200 extends DataResponse<UserLoginDto> {
    public KakaoLogin_200(String status, String message, UserLoginDto data) {
        super(status, message, data);
    }
}
