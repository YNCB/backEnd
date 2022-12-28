package AMS.AMSsideproject;

import AMS.AMSsideproject.domain.like.service.LikeService;
import AMS.AMSsideproject.domain.post.Post;
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
    private final LikeService likeService;

    @PostConstruct
    public void init() {

        //사용자 데이터
        UserJoinForm2 userJoinForm1 = new UserJoinForm2("test1@naver.com","test1", "test1", "basic", "학생", "Java");
        User join1 = userService.join(userJoinForm1);
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test2@google.com", "test2", "test2", "basic", "학생","Java");
        User join2 = userService.join(userJoinForm2);
        UserJoinForm2 userJoinForm3 = new UserJoinForm2("test3@google.com", "test3", "test3", "basic", "학생","Java");
        User join3 = userService.join(userJoinForm3);
        UserJoinForm2 userJoinForm4 = new UserJoinForm2("test4@naver.com", "test4", "test4", "basic", "학생","Java");
        User join4 = userService.join(userJoinForm4);
        UserJoinForm2 userJoinForm5 = new UserJoinForm2("test5@google.com", "test5", "test5", "basic", "학생","Java");
        User join5 = userService.join(userJoinForm5);

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
        Post registration1 = postService.registration(join1.getUser_id(), postSaveForm1);
        Post registration2 = postService.registration(join1.getUser_id(), postSaveForm2);
        Post registration3 = postService.registration(join1.getUser_id(), postSaveForm3);
        Post registration4 = postService.registration(join1.getUser_id(), postSaveForm4);
        Post registration5 = postService.registration(join1.getUser_id(), postSaveForm5);
        Post registration6 = postService.registration(join1.getUser_id(), postSaveForm6);

        List<String> tags4 = new ArrayList<>(); tags1.add("DFS"); tags1.add("BFS");
        PostSaveForm postSaveForm7 = new PostSaveForm(tags4, "post7 test", "post7","post7","see","Java",3);
        PostSaveForm postSaveForm8 = new PostSaveForm(tags4, "post8 test", "post8", "post8", "see", "Java", 5);
        List<String> tags5 = new ArrayList<>(); tags2.add("DFS");
        PostSaveForm postSaveForm9 = new PostSaveForm(tags5, "post9 test", "post9", "post9", "alone", "Java", 2);
        PostSaveForm postSaveForm10 = new PostSaveForm(tags5, "post10 test", "post10", "post10", "alone", "Python", 3);
        List<String> tags6 = new ArrayList<>(); tags3.add("BFS");
        PostSaveForm postSaveForm11 = new PostSaveForm(tags6, "post11 test", "post11", "post11", "alone", "Java", 4);
        PostSaveForm postSaveForm12 = new PostSaveForm(tags6, "post12 test", "post12", "post12", "see", "Java", 5);
        Post registration7 = postService.registration(join2.getUser_id(), postSaveForm7);
        Post registration8 = postService.registration(join2.getUser_id(), postSaveForm8);
        Post registration9 = postService.registration(join2.getUser_id(), postSaveForm9);
        Post registration10 = postService.registration(join2.getUser_id(), postSaveForm10);
        Post registration11 = postService.registration(join2.getUser_id(), postSaveForm11);
        Post registration12 = postService.registration(join2.getUser_id(), postSaveForm12);

        //좋아요 데이터
        likeService.like_v2(registration1.getPost_id(), join1.getUser_id());
        likeService.like_v2(registration1.getPost_id(), join2.getUser_id());
        likeService.like_v2(registration1.getPost_id(), join3.getUser_id());
        likeService.like_v2(registration1.getPost_id(), join4.getUser_id());
        likeService.like_v2(registration1.getPost_id(), join5.getUser_id());

        likeService.like_v2(registration7.getPost_id(), join1.getUser_id());
        likeService.like_v2(registration7.getPost_id(), join2.getUser_id());
        likeService.like_v2(registration7.getPost_id(), join3.getUser_id());
        likeService.like_v2(registration7.getPost_id(), join4.getUser_id());
        likeService.like_v2(registration7.getPost_id(), join5.getUser_id());


    }
}
