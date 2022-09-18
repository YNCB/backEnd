package AMS.AMSsideproject.web.swagger.userController;

import AMS.AMSsideproject.web.apiController.user.requestDto.ValidNickNameDto;
import AMS.AMSsideproject.web.response.DataResponse;

public class ValidDuplicateNickName_400 extends DataResponse<ValidNickNameDto> {
    public ValidDuplicateNickName_400(String status, String message, ValidNickNameDto data) {
        super(status, message, data);
    }
}
