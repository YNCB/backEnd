package AMS.AMSsideproject.web.swagger.postController;

import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.response.post.PostListResponse;
import AMS.AMSsideproject.web.responseDto.post.PostListDtoAboutSpecificUser;

import java.util.List;

public class UserPage_200 extends DataResponse<PostListResponse<List<PostListDtoAboutSpecificUser>>> {

    public UserPage_200(String status, String message, PostListResponse<List<PostListDtoAboutSpecificUser>> data) {
        super(status, message, data);
    }
}
