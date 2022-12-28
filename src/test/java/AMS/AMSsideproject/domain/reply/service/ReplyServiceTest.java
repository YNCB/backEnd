package AMS.AMSsideproject.domain.reply.service;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.service.PostService;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.post.requestForm.PostSaveForm;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserJoinForm2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyServiceTest {

    @Autowired ReplyService replyService;
    @Autowired PostService postService;
    @Autowired UserService userService;

    @BeforeEach
    public void init() {
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test@naver.com", "test1234", "test", "basic", "학생","java");
        User join = userService.join(userJoinForm2);

        List<String> tags = new ArrayList<>(); tags.add("DFS");
        PostSaveForm postSaveForm = new PostSaveForm(tags, "test", "test", "test", "see", "java",3);
        Post registration = postService.registration(join.getUser_id(), postSaveForm);
    }

    @Test
    @Transactional
    public void 댓글저장테스트() throws Exception {
        //given

        //when

        //then
    }

    @Test
    public void 댓글조회테스트() throws Exception {
        //given

        //when

        //then
    }

}