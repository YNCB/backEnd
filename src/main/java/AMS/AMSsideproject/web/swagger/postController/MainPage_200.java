package AMS.AMSsideproject.web.swagger.postController;

import AMS.AMSsideproject.web.response.DataResponse;
import AMS.AMSsideproject.web.response.post.MainPagePostListDto;
import AMS.AMSsideproject.web.responseDto.post.PostListDtoAboutAllUser;

import java.util.List;

public class MainPage_200 extends DataResponse<MainPagePostListDto<List<PostListDtoAboutAllUser>>> {

    public MainPage_200(String status, String message, MainPagePostListDto<List<PostListDtoAboutAllUser>> data) {
        super(status, message, data);
    }
}
