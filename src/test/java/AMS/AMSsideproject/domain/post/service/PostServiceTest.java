package AMS.AMSsideproject.domain.post.service;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.repository.form.SearchFormAboutAllUser;
import AMS.AMSsideproject.domain.tag.Tag.Tag;
import AMS.AMSsideproject.domain.tag.Tag.repository.TagRepository;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.post.requestDto.PostSaveForm;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserJoinForm2;
import AMS.AMSsideproject.web.test.DatabaseCleanup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Slice;
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
    public void init() {
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test2@gamil.com", "test1234!", "test2", "Basic","Student","Java");
        User findUser = userService.join(userJoinForm2);

        List<String> tags = new ArrayList<>();
        tags.add("BFS"); tags.add("DFS");
        PostSaveForm postSaveForm1 = new PostSaveForm(tags,"koo1", "koo1","koo1", "보고 푼 문제","C++",1);
        PostSaveForm postSaveForm2 = new PostSaveForm(tags,"koo2", "koo2","koo2", "보고 푼 문제","Java",3);
        PostSaveForm postSaveForm3 = new PostSaveForm(tags,"koo3", "koo3","koo3", "보고 푼 문제","Java",2);
        PostSaveForm postSaveForm4 = new PostSaveForm(tags,"koo4", "koo4","koo4", "보고 푼 문제","Java",5);
        PostSaveForm postSaveForm5 = new PostSaveForm(tags,"koo5", "koo5","koo5", "보고 푼 문제","Java",6);
        PostSaveForm postSaveForm6 = new PostSaveForm(tags,"koo6", "koo6","koo6", "보고 푼 문제","Java",6);
        PostSaveForm postSaveForm7 = new PostSaveForm(tags,"koo7", "koo7","koo7", "보고 푼 문제","Java",8);
        PostSaveForm postSaveForm8 = new PostSaveForm(tags,"koo8", "koo8","koo8", "보고 푼 문제","Java",6);

        Post post1 = postService.registration(findUser.getUser_id(), postSaveForm1);
        postService.addPostLikeNum(post1.getPost_id(), 1L);
        Post post2 = postService.registration(findUser.getUser_id(), postSaveForm2);
        postService.addPostLikeNum(post2.getPost_id(), 3L);
        Post post3 = postService.registration(findUser.getUser_id(), postSaveForm3);
        postService.addPostLikeNum(post3.getPost_id(), 2L);
        Post post4 = postService.registration(findUser.getUser_id(), postSaveForm4);
        postService.addPostLikeNum(post4.getPost_id(), 5L);
        Post post5 = postService.registration(findUser.getUser_id(), postSaveForm5);
        postService.addPostLikeNum(post5.getPost_id(), 6L);
        Post post6 = postService.registration(findUser.getUser_id(), postSaveForm6);
        postService.addPostLikeNum(post6.getPost_id(), 6L);
        Post post7 = postService.registration(findUser.getUser_id(), postSaveForm7);
        postService.addPostLikeNum(post7.getPost_id(), 8L);
        Post post8 = postService.registration(findUser.getUser_id(), postSaveForm8);
        postService.addPostLikeNum(post8.getPost_id(), 6L);
    }

    @Autowired private DatabaseCleanup databaseCleanup;
    @AfterEach
    public void clear() {
        databaseCleanup.execute();
    }

    @Test
    @Transactional
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

    @Test
    @Transactional
    public void 모든유저게시물에대해서필터링조회최신순정렬무한스크롤첫페이지() throws Exception {

        //when
        SearchFormAboutAllUser form = new SearchFormAboutAllUser("Java", "koo", "redate", null, null,null);
        Slice<Post> findPosts = postService.findAboutAllUserPost(form);

        //then
        boolean hasNext = findPosts.hasNext();
        List<Post> content = findPosts.getContent();

        Assertions.assertThat(hasNext).isTrue();
        Assertions.assertThat(content.size()).isEqualTo(3);
    }

    @Test
    @Transactional
    public void 모든유저게시물에대해서필터링조회최신순정렬무한스크롤중간페이지() throws Exception {

        //when
        SearchFormAboutAllUser form = new SearchFormAboutAllUser("Java", "koo", "redate", 6L, null,null);
        Slice<Post> findPosts = postService.findAboutAllUserPost(form);

        //then
        boolean hasNext = findPosts.hasNext();
        List<Post> content = findPosts.getContent();

        Assertions.assertThat(hasNext).isTrue();
        Assertions.assertThat(content.size()).isEqualTo(3);

    }

    @Test
    @Transactional
    public void 모든유저게시물에대해서필터링조회최신순정렬무한스크롤마지막페이지() throws Exception {

        //when
        SearchFormAboutAllUser form = new SearchFormAboutAllUser("Java", "koo", "redate", 3L, null,null);
        Slice<Post> findPosts = postService.findAboutAllUserPost(form);

        //then
        boolean hasNext = findPosts.hasNext();
        List<Post> content = findPosts.getContent();

        Assertions.assertThat(hasNext).isFalse();
        Assertions.assertThat(content.size()).isEqualTo(1);
    }

    @Test
    @Transactional
    public void 모든유저게시물에대해서필터링조회좋아요순정렬무한스크롤첫페이지() throws Exception {

        //when
        SearchFormAboutAllUser form = new SearchFormAboutAllUser("Java", "koo", "likeNum", null, null,null);
        Slice<Post> findPosts = postService.findAboutAllUserPost(form);

        //then
        boolean hasNext = findPosts.hasNext();
        List<Post> content = findPosts.getContent();

        Assertions.assertThat(hasNext).isTrue();
        Assertions.assertThat(content.size()).isEqualTo(3);
    }

    @Test
    @Transactional
    public void 모든유저게시물에대해서필터링조회좋아요순정렬무한스크롤중간페이지() throws Exception {

        //when
        SearchFormAboutAllUser form = new SearchFormAboutAllUser("Java", "koo", "likeNum", 6L, null,6L);
        Slice<Post> findPosts = postService.findAboutAllUserPost(form);

        //then
        boolean hasNext = findPosts.hasNext();
        List<Post> content = findPosts.getContent();

        Assertions.assertThat(hasNext).isTrue();
        Assertions.assertThat(content.size()).isEqualTo(3);
    }

    @Test
    @Transactional
    public void 모든유저게시물에대해서필터링조회좋아요순정렬무한스크롤마지막페이지() throws Exception {

        //when
        SearchFormAboutAllUser form = new SearchFormAboutAllUser("Java", "koo", "likeNum", 2L, null,3L);
        Slice<Post> findPosts = postService.findAboutAllUserPost(form);

        //then
        boolean hasNext = findPosts.hasNext();
        List<Post> content = findPosts.getContent();

        Assertions.assertThat(hasNext).isFalse();
        Assertions.assertThat(content.size()).isEqualTo(1);
    }

//    @Test
//    @Transactional
//    @Rollback(value = false)
//    public void PostListDto테스트() throws Exception {
//
//        //when
//        SearchFormAboutAllUser form = new SearchFormAboutAllUser("Java", "koo", "redate", null, null,null);
//        Slice<Post> findPosts = postService.findAboutAllUserPost(form);
//
//        //then
//        List<Post> posts = findPosts.getContent();
//        for(Post p : posts) {
//            System.out.println(p);
//            System.out.println("======================");
//        }
//    }

}