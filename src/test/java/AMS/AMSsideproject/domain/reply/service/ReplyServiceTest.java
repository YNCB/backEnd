package AMS.AMSsideproject.domain.reply.service;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.service.PostService;
import AMS.AMSsideproject.domain.reply.Reply;
import AMS.AMSsideproject.domain.reply.repository.ReplyRepository;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.post.requestForm.PostSaveForm;
import AMS.AMSsideproject.web.apiController.reply.requestForm.ReplyEditForm;
import AMS.AMSsideproject.web.apiController.reply.requestForm.ReplySaveForm;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserJoinForm2;
import AMS.AMSsideproject.web.exception.NotUserEq;
import AMS.AMSsideproject.web.responseDto.reply.RepliesDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ReplyServiceTest {

    @Autowired ReplyService replyService;
    @Autowired ReplyRepository replyRepository;
    @Autowired PostService postService;
    @Autowired UserService userService;

    @Test
    @Transactional
    public void 댓글저장테스트() throws Exception {

        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test@naver.com", "test1234", "test", "basic", "학생","java");
        User newUser = userService.join(userJoinForm2);

        List<String> tags = new ArrayList<>(); tags.add("DFS");
        PostSaveForm postSaveForm = new PostSaveForm(tags, "test", "test", "test", "see", "java",3);
        Post newPost = postService.registration(newUser.getUser_id(), postSaveForm);

        //given
        ReplySaveForm replySaveForm1 = new ReplySaveForm("test1", "test1", null);

        //when
        Reply saveReply = replyService.addReply(newPost.getPost_id(), newUser.getUser_id(), replySaveForm1);

        //then
        Assertions.assertThat(saveReply.getParent()).isNull();
        Assertions.assertThat(newPost.getReplyNum()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void 대댓글저장테스트() throws Exception {
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test@naver.com", "test1234", "test", "basic", "학생","java");
        User newUser = userService.join(userJoinForm2);

        List<String> tags = new ArrayList<>(); tags.add("DFS");
        PostSaveForm postSaveForm = new PostSaveForm(tags, "test", "test", "test", "see", "java",3);
        Post newPost = postService.registration(newUser.getUser_id(), postSaveForm);

        //given
        ReplySaveForm replySaveForm1 = new ReplySaveForm("test1", "test1", null);
        Reply saveReply1 = replyService.addReply(newPost.getPost_id(), newUser.getUser_id(), replySaveForm1);

        //when
        ReplySaveForm replySaveForm2 = new ReplySaveForm("test2", "test2", saveReply1.getReply_id());
        Reply saveReply2 = replyService.addReply(newPost.getPost_id(), newUser.getUser_id(), replySaveForm2);

        //then
        Assertions.assertThat(newPost.getReplyNum()).isEqualTo(1); //루트댓글일때만 댓글수 증가
        Assertions.assertThat(saveReply2.getParent().getReply_id()).isEqualTo(saveReply1.getReply_id());
        Assertions.assertThat(saveReply1.getChildren().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void 대대댓글저장테스트() throws Exception {
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test@naver.com", "test1234", "test", "basic", "학생","java");
        User newUser = userService.join(userJoinForm2);

        List<String> tags = new ArrayList<>(); tags.add("DFS");
        PostSaveForm postSaveForm = new PostSaveForm(tags, "test", "test", "test", "see", "java",3);
        Post newPost = postService.registration(newUser.getUser_id(), postSaveForm);

        //given
        ReplySaveForm replySaveForm1 = new ReplySaveForm("test1", "test1", null);
        Reply saveReply1 = replyService.addReply(newPost.getPost_id(), newUser.getUser_id(), replySaveForm1);
        ReplySaveForm replySaveForm2 = new ReplySaveForm("test2", "test2", saveReply1.getReply_id());
        Reply saveReply2 = replyService.addReply(newPost.getPost_id(), newUser.getUser_id(), replySaveForm2);

        //when
        ReplySaveForm replySaveForm3 = new ReplySaveForm("test3", "test3", saveReply2.getReply_id());
        Reply saveReply3 = replyService.addReply(newPost.getPost_id(), newUser.getUser_id(), replySaveForm3);

        //then
        Assertions.assertThat(newPost.getReplyNum()).isEqualTo(1);
        Assertions.assertThat(saveReply3.getParent().getReply_id()).isEqualTo(saveReply2.getReply_id());
        Assertions.assertThat(saveReply1.getChildren().size()).isEqualTo(1);
        Assertions.assertThat(saveReply2.getChildren().size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void 댓글리스트조회테스트() throws Exception {
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test@naver.com", "test1234", "test", "basic", "학생","java");
        User newUser = userService.join(userJoinForm2);

        List<String> tags = new ArrayList<>(); tags.add("DFS");
        PostSaveForm postSaveForm = new PostSaveForm(tags, "test", "test", "test", "see", "java",3);
        Post newPost = postService.registration(newUser.getUser_id(), postSaveForm);

        //given
        ReplySaveForm replySaveForm1 = new ReplySaveForm("test1", "test1", null);
        Reply saveReply1 = replyService.addReply(newPost.getPost_id(), newUser.getUser_id(), replySaveForm1);
        ReplySaveForm replySaveForm2 = new ReplySaveForm("test2", "test2", saveReply1.getReply_id());
        Reply saveReply2 = replyService.addReply(newPost.getPost_id(), newUser.getUser_id(), replySaveForm2);
        ReplySaveForm replySaveForm3 = new ReplySaveForm("test3", "test3", saveReply2.getReply_id());
        Reply saveReply3 = replyService.addReply(newPost.getPost_id(), newUser.getUser_id(), replySaveForm3);

        //when
        List<RepliesDto> findReplies = replyService.findRepliesByPostId(newPost.getPost_id());

        //then
        System.out.println(findReplies);
    }

    @Test
    public void 댓글삭제테스트() throws Exception {
        //given
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test@naver.com", "test1234", "test", "basic", "학생","java");
        User newUser = userService.join(userJoinForm2);

        List<String> tags = new ArrayList<>(); tags.add("DFS");
        PostSaveForm postSaveForm = new PostSaveForm(tags, "test", "test", "test", "see", "java",3);
        Post newPost = postService.registration(newUser.getUser_id(), postSaveForm);

        ReplySaveForm replySaveForm1 = new ReplySaveForm("reply1", "test1", null);
        Reply saveReply1 = replyService.addReply(newPost.getPost_id(), newUser.getUser_id(), replySaveForm1);
        ReplySaveForm replySaveForm2 = new ReplySaveForm("reply2", "test2", saveReply1.getReply_id());
        Reply saveReply2 = replyService.addReply(newPost.getPost_id(), newUser.getUser_id(), replySaveForm2);
        ReplySaveForm replySaveForm3 = new ReplySaveForm("reply3", "test3", saveReply2.getReply_id());
        Reply saveReply3 = replyService.addReply(newPost.getPost_id(), newUser.getUser_id(), replySaveForm3);

        //when
        replyService.deleteReply(newPost.getPost_id(), saveReply2.getReply_id(), newUser.getUser_id());

        //then
        List<Reply> replies = replyRepository.findReplies(newPost.getPost_id());
        Assertions.assertThat(replies.size()).isEqualTo(1);

    }

    @Test()
    public void 댓글삭제테스트권한에러() throws Exception {
        //given
        UserJoinForm2 userJoinForm1 = new UserJoinForm2("test@naver.com", "test1234", "test", "basic", "학생","java");
        User newUser1 = userService.join(userJoinForm1);
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test@google.com", "test1234", "test", "basic", "학생","java");
        User newUser2 = userService.join(userJoinForm2);


        List<String> tags = new ArrayList<>(); tags.add("DFS");
        PostSaveForm postSaveForm = new PostSaveForm(tags, "test", "test", "test", "see", "java",3);
        Post newPost = postService.registration(newUser1.getUser_id(), postSaveForm);

        ReplySaveForm replySaveForm1 = new ReplySaveForm("test1", "test1", null);
        Reply saveReply1 = replyService.addReply(newPost.getPost_id(), newUser1.getUser_id(), replySaveForm1);

        //when ,then
        Assertions.assertThatThrownBy( () -> replyService.deleteReply(newPost.getPost_id(), saveReply1.getReply_id(), newUser2.getUser_id()))
                .isInstanceOf(NotUserEq.class);

    }

    @Test
    public void 댓글수정테스트() throws Exception {
        //given
        UserJoinForm2 userJoinForm1 = new UserJoinForm2("test@naver.com", "test1234", "test", "basic", "학생","java");
        User newUser1 = userService.join(userJoinForm1);

        List<String> tags = new ArrayList<>(); tags.add("DFS");
        PostSaveForm postSaveForm = new PostSaveForm(tags, "test", "test", "test", "see", "java",3);
        Post newPost = postService.registration(newUser1.getUser_id(), postSaveForm);

        ReplySaveForm replySaveForm1 = new ReplySaveForm("test1", "test1", null);
        Reply beforeReply = replyService.addReply(newPost.getPost_id(), newUser1.getUser_id(), replySaveForm1);

        //when
        ReplyEditForm replyEditForm = new ReplyEditForm("edit1", "edit1");
        replyService.updateReply(newUser1.getUser_id(), beforeReply.getReply_id(), replyEditForm);

        //then
        Reply afterReply = replyRepository.findReply(beforeReply.getReply_id());
        Assertions.assertThat(afterReply.getTitle()).isNotEqualTo(beforeReply.getTitle());
    }

}