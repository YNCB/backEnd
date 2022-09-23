package AMS.AMSsideproject.domain.post.service;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.tag.Tag.Tag;
import AMS.AMSsideproject.domain.tag.Tag.repository.TagRepository;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.post.requestDto.PostSaveForm;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserJoinForm2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class PostServiceTest {
    @Autowired UserService userService;
    @Autowired PostService postService;
    @Autowired TagRepository tagRepository;

    @BeforeEach
    @Transactional
    public void init() {
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test2@gamil.com", "test1234!", "test2", "Basic","Student","Java");
        userService.join(userJoinForm2);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void 게시물등록테스트() throws Exception {
        //given
        User findUser = userService.findUserByNickName("test2");

        List<String> tags = new ArrayList<>();
        tags.add("BFS"); tags.add("DFS");
        PostSaveForm postSaveForm = new PostSaveForm(tags,"test", "test","test", "보고 푼 문제","Java",4);

        //when
        Post addPost = postService.registration(findUser.getUser_id(), postSaveForm);

        //then
        Assertions.assertThat(addPost.getPostTagList().size()).isEqualTo(2);
        for(String tagName: tags) {
            Optional<Tag> findTag = tagRepository.findByTagName(tagName);
            Assertions.assertThat(findTag.get()).isNotNull();
        }
    }

}