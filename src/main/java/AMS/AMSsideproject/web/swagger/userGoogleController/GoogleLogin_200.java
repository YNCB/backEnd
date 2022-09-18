package AMS.AMSsideproject.web.swagger.userGoogleController;

import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.user.UserLoginDto;

public class GoogleLogin_200 extends DataResponse<UserLoginDto> {
    public GoogleLogin_200(String status, String message, UserLoginDto data) {
        super(status, message, data);
    }
}
