package AMS.AMSsideproject;

import AMS.AMSsideproject.domain.post.repository.PostRepository;
import AMS.AMSsideproject.domain.post.service.PostService;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.repository.UserRepository;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.post.requestForm.PostSaveForm;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserJoinForm2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TestDataInit {

    private final UserService userService;
    private final PostService postService;

    @PostConstruct
    public void init() {

        //사용자 데이터
        UserJoinForm2 userJoinForm1 = new UserJoinForm2("test1@naver.com","test1", "test1", "basic", "학생", "Java");
        User join1 = userService.join(userJoinForm1);
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test2@google.com", "test2", "test2", "kakao", "학생","Java");
        User join2 = userService.join(userJoinForm2);

        //게시물 데이터
        List<String> tags1 = new ArrayList<>(); tags1.add("DFS"); tags1.add("BFS");
        PostSaveForm postSaveForm1 = new PostSaveForm(tags1, "post1 test", "post1","post1","see","Java",3);
        PostSaveForm postSaveForm2 = new PostSaveForm(tags1, "post2 test", "post2", "post2", "see", "Java", 5);
        List<String> tags2 = new ArrayList<>(); tags2.add("Greedy");
        PostSaveForm postSaveForm3 = new PostSaveForm(tags2, "post3 test", "post3", "post3", "alone", "Java", 2);
        PostSaveForm postSaveForm4 = new PostSaveForm(tags2, "post4 test", "post4", "post4", "alone", "Python", 3);
        List<String> tags3 = new ArrayList<>(); tags3.add("DFS"); tags3.add("Graph");
        PostSaveForm postSaveForm5 = new PostSaveForm(tags3, "post5 test", "post5", "post5", "alone", "Java", 4);
        PostSaveForm postSaveForm6 = new PostSaveForm(tags3, "post6 test", "post6", "post6", "see", "Java", 5);

        postService.registration(join1.getUser_id(), postSaveForm1);
        postService.registration(join1.getUser_id(), postSaveForm2);
        postService.registration(join1.getUser_id(), postSaveForm3);
        postService.registration(join1.getUser_id(), postSaveForm4);
        postService.registration(join1.getUser_id(), postSaveForm5);
        postService.registration(join1.getUser_id(), postSaveForm6);




    }
}
