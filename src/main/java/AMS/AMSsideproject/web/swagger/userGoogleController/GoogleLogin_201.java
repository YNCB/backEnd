package AMS.AMSsideproject.web.swagger.userGoogleController;

import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.responseDto.user.GoogleUserJoinDto;

public class GoogleLogin_201 extends DataResponse<GoogleUserJoinDto> {
    public GoogleLogin_201(String status, String message, GoogleUserJoinDto data) {
        super(status, message, data);
    }
}
