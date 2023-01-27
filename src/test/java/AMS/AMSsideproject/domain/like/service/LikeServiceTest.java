package AMS.AMSsideproject.domain.like.service;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.Type;
import AMS.AMSsideproject.domain.post.service.PostServiceImplV1;
import AMS.AMSsideproject.domain.user.Job;
import AMS.AMSsideproject.domain.user.LoginType;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.post.requestForm.PostSaveForm;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserJoinForm2;
import AMS.AMSsideproject.web.responseDto.like.LikeDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class LikeServiceTest {

    @Autowired
    LikeService likeService;
    @Autowired
    UserService userService;
    @Autowired
    PostServiceImplV1 postService;


    @Test
    @Transactional
    public void 좋아요_version2_추가테스트() throws Exception {
        //given
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test", "test", "test", LoginType.BASIC.name(), Job.학생.name(), "Java");
        User join = userService.join(userJoinForm2);

        List<String> tag = new ArrayList<>(); tag.add("BFS");
        PostSaveForm postSaveForm = new PostSaveForm(tag, "test", "test", "test", Type.SEE.name(), "test" ,1);
        Post registration = postService.registration(join.getUser_id(), postSaveForm);

        //when
        LikeDto likeDto = likeService.like_v2(registration.getPost_id(), join.getUser_id());

        //then
        System.out.println("likeDto : " + likeDto);
    }

}