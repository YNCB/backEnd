package AMS.AMSsideproject.domain.post.repository;

import AMS.AMSsideproject.domain.post.service.PostService;
import AMS.AMSsideproject.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class PostRepositoryImplTest {

    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    @Test
    @Transactional
    public void 무한스크롤테스트() throws Exception {
//        //given
//        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test3@gamil.com", "test1234!", "test3", "Basic","Student","Java");
//        userService.join(userJoinForm2);
//
//        User findUser = userService.findUserByNickName("test3");
//
//        List<String> tags = new ArrayList<>();
//        tags.add("BFS"); tags.add("DFS");
//        PostSaveForm postSaveForm1 = new PostSaveForm(tags,"koo1", "koo1","koo1", "보고 푼 문제","Java",1);
//        PostSaveForm postSaveForm2 = new PostSaveForm(tags,"koo2", "koo2","koo2", "보고 푼 문제","Java",3);
//        PostSaveForm postSaveForm3 = new PostSaveForm(tags,"koo3", "koo3","koo3", "보고 푼 문제","Java",6);
//        PostSaveForm postSaveForm4 = new PostSaveForm(tags,"koo4", "koo4","koo4", "보고 푼 문제","Java",5);
//        PostSaveForm postSaveForm5 = new PostSaveForm(tags,"koo5", "koo5","koo5", "보고 푼 문제","Java",4);
//        PostSaveForm postSaveForm6 = new PostSaveForm(tags,"koo6", "koo6","koo6", "보고 푼 문제","Java",2);
//
//        postService.registration(findUser.getUser_id(), postSaveForm1);
//        postService.registration(findUser.getUser_id(), postSaveForm2);
//        postService.registration(findUser.getUser_id(), postSaveForm3);
//        postService.registration(findUser.getUser_id(), postSaveForm4);
//        postService.registration(findUser.getUser_id(), postSaveForm5);
//        postService.registration(findUser.getUser_id(), postSaveForm6);
//
//        List<Sort.Order> orders = new ArrayList<>();
//        orders.add(Sort.Order.desc("level"));
//
//        //when
//        //1
//        Pageable pageable1 = PageRequest.of(0,4, Sort.by(orders));
//        Slice<Post> stores1 = postRepository.slice(null, "Java", "koo", pageable1);
//
//        //2
//        Pageable pageable2 = PageRequest.of(0,4, Sort.by(orders));
//        Slice<Post> stores2 = postRepository.slice(2L, "Java", "koo", pageable2);
//
//        //then
//        //1
//        Assertions.assertThat(stores1.hasNext()).isTrue();
//
//        //2
//        Assertions.assertThat(stores2.hasNext()).isFalse();
//        Assertions.assertThat(stores2.getContent().size()).isEqualTo(2);
//
        
    }

}