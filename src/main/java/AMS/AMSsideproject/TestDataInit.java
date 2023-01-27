package AMS.AMSsideproject;

import AMS.AMSsideproject.domain.like.service.LikeService;
import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.service.PostServiceImplV1;
import AMS.AMSsideproject.domain.reply.Reply;
import AMS.AMSsideproject.domain.reply.service.ReplyService;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.post.requestForm.PostSaveForm;
import AMS.AMSsideproject.web.apiController.reply.requestForm.ReplySaveForm;
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
    private final PostServiceImplV1 postService;
    private final LikeService likeService;
    private final ReplyService replyService;

    @PostConstruct
    public void init() {

        //사용자 데이터
        UserJoinForm2 userJoinForm1 = new UserJoinForm2("data1@naver.com","data1", "data1", "BASIC", "학생", "Java");
        User join1 = userService.join(userJoinForm1);
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("data2@google.com", "data2", "data2", "BASIC", "학생","Java");
        User join2 = userService.join(userJoinForm2);
        UserJoinForm2 userJoinForm3 = new UserJoinForm2("data3@google.com", "data3", "data3", "BASIC", "학생","Java");
        User join3 = userService.join(userJoinForm3);
        UserJoinForm2 userJoinForm4 = new UserJoinForm2("data4@naver.com", "data4", "data4", "BASIC", "학생","Java");
        User join4 = userService.join(userJoinForm4);
        UserJoinForm2 userJoinForm5 = new UserJoinForm2("data5@google.com", "data5", "data5", "BASIC", "학생","Java");
        User join5 = userService.join(userJoinForm5);

        //게시물 데이터
        List<String> tags1 = new ArrayList<>(); tags1.add("DFS"); tags1.add("BFS");
        PostSaveForm postSaveForm1 = new PostSaveForm(tags1, "post1 test", "post1","post1","SEE","Java",3);
        PostSaveForm postSaveForm2 = new PostSaveForm(tags1, "post2 test", "post2", "post2", "SEE", "Java", 5);
        List<String> tags2 = new ArrayList<>(); tags2.add("Greedy");
        PostSaveForm postSaveForm3 = new PostSaveForm(tags2, "post3 test", "post3", "post3", "ALONE", "Java", 2);
        PostSaveForm postSaveForm4 = new PostSaveForm(tags2, "post4 test", "post4", "post4", "ALONE", "Python", 3);
        List<String> tags3 = new ArrayList<>(); tags3.add("DFS"); tags3.add("Graph");
        PostSaveForm postSaveForm5 = new PostSaveForm(tags3, "post5 test", "post5", "post5", "ALONE", "Java", 4);
        PostSaveForm postSaveForm6 = new PostSaveForm(tags3, "post6 test", "post6", "post6", "SEE", "Java", 5);
        Post registration1 = postService.registration(join1.getUser_id(), postSaveForm1);
        Post registration2 = postService.registration(join1.getUser_id(), postSaveForm2);
        Post registration3 = postService.registration(join1.getUser_id(), postSaveForm3);
        Post registration4 = postService.registration(join1.getUser_id(), postSaveForm4);
        Post registration5 = postService.registration(join1.getUser_id(), postSaveForm5);
        Post registration6 = postService.registration(join1.getUser_id(), postSaveForm6);

        List<String> tags4 = new ArrayList<>(); tags1.add("DFS"); tags1.add("BFS");
        PostSaveForm postSaveForm7 = new PostSaveForm(tags4, "post7 test", "post7","post7","SEE","Java",3);
        PostSaveForm postSaveForm8 = new PostSaveForm(tags4, "post8 test", "post8", "post8", "SEE", "Java", 5);
        List<String> tags5 = new ArrayList<>(); tags2.add("DFS");
        PostSaveForm postSaveForm9 = new PostSaveForm(tags5, "post9 test", "post9", "post9", "ALONE", "Java", 2);
        PostSaveForm postSaveForm10 = new PostSaveForm(tags5, "post10 test", "post10", "post10", "ALONE", "Python", 3);
        List<String> tags6 = new ArrayList<>(); tags3.add("BFS");
        PostSaveForm postSaveForm11 = new PostSaveForm(tags6, "post11 test", "post11", "post11", "ALONE", "Java", 4);
        PostSaveForm postSaveForm12 = new PostSaveForm(tags6, "post12 test", "post12", "post12", "SEE", "Java", 5);
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


        //댓글 데이터
        ReplySaveForm replySaveForm1 = new ReplySaveForm("reply1", "reply1", null);
        Reply saveReply1 = replyService.addReply(registration1.getPost_id(), join3.getUser_id(), replySaveForm1);
        ReplySaveForm replySaveForm2 = new ReplySaveForm("reply2", "reply2", saveReply1.getReply_id());
        Reply saveReply2 = replyService.addReply(registration1.getPost_id(), join4.getUser_id(), replySaveForm2);
        ReplySaveForm replySaveForm3 = new ReplySaveForm("reply3", "reply3", saveReply2.getReply_id());
        Reply saveReply3 = replyService.addReply(registration1.getPost_id(), join5.getUser_id(), replySaveForm3);
        ReplySaveForm replySaveForm4 = new ReplySaveForm("reply4", "reply4", null);
        Reply saveReply4 = replyService.addReply(registration1.getPost_id(), join2.getUser_id(), replySaveForm1);

        //팔로우 데이터

    }
}
