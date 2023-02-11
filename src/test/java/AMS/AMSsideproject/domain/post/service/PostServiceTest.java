package AMS.AMSsideproject.domain.post.service;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.Type;
import AMS.AMSsideproject.domain.post.repository.PostRepositoryV1;
import AMS.AMSsideproject.domain.user.Job;
import AMS.AMSsideproject.domain.user.LoginType;
import AMS.AMSsideproject.web.apiController.post.OrderKey;
import AMS.AMSsideproject.web.apiController.post.requestForm.*;
import AMS.AMSsideproject.domain.tag.Tag.Tag;
import AMS.AMSsideproject.domain.tag.Tag.repository.TagRepository;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.service.UserService;
import AMS.AMSsideproject.web.apiController.user.requestDto.UserJoinForm2;
import AMS.AMSsideproject.web.responseDto.post.PostListDtoAboutAllUser;
import AMS.AMSsideproject.web.test.DatabaseCleanup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Slice;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
class PostServiceTest {

    @Autowired UserService userService;
    @Autowired PostServiceImplV1 postServiceImplV1;
    @Autowired TagRepository tagRepository;
    @Autowired PostRepositoryV1 postRepository;
    @Autowired HttpServletRequest request;
    @Autowired HttpServletResponse response;

    @Autowired PostService postService;

    @BeforeEach
    public void init() {
        UserJoinForm2 userJoinForm2 = new UserJoinForm2("test2@gamil.com", "test1234!", "test2", LoginType.BASIC.name(), Job.학생.name(),"Java");
        User findUser = userService.join(userJoinForm2);

        List<String> tags = new ArrayList<>();
        tags.add("BFS"); tags.add("DFS");
        PostSaveForm postSaveForm1 = new PostSaveForm(tags,"koo1", "koo1","koo1", Type.SEE.name(),"C++",1);
        PostSaveForm postSaveForm2 = new PostSaveForm(tags,"koo2", "koo2","koo2", Type.SEE.name(),"Java",3);
        PostSaveForm postSaveForm3 = new PostSaveForm(tags,"koo3", "koo3","koo3", Type.SEE.name(),"Java",2);
        PostSaveForm postSaveForm4 = new PostSaveForm(tags,"koo4", "koo4","koo4", Type.SEE.name(),"Java",5);
        PostSaveForm postSaveForm5 = new PostSaveForm(tags,"koo5", "koo5","koo5", Type.SEE.name(),"Java",5);
        PostSaveForm postSaveForm6 = new PostSaveForm(tags,"koo6", "koo6","koo6", Type.SEE.name(),"Java",5);
        PostSaveForm postSaveForm7 = new PostSaveForm(tags,"koo7", "koo7","koo7", Type.SEE.name(),"Java",4);
        PostSaveForm postSaveForm8 = new PostSaveForm(tags,"koo8", "koo8","koo8", Type.SEE.name(),"Java",4);

        Post post1 = postServiceImplV1.registration(findUser.getUser_id(), postSaveForm1);
        Post post2 = postServiceImplV1.registration(findUser.getUser_id(), postSaveForm2);
        Post post3 = postServiceImplV1.registration(findUser.getUser_id(), postSaveForm3);
        Post post4 = postServiceImplV1.registration(findUser.getUser_id(), postSaveForm4);
        Post post5 = postServiceImplV1.registration(findUser.getUser_id(), postSaveForm5);
        Post post6 = postServiceImplV1.registration(findUser.getUser_id(), postSaveForm6);
        Post post7 = postServiceImplV1.registration(findUser.getUser_id(), postSaveForm7);
        Post post8 = postServiceImplV1.registration(findUser.getUser_id(), postSaveForm8);

        UserJoinForm2 userJoinForm3 = new UserJoinForm2("test3@gamil.com", "test1234!", "test3", LoginType.BASIC.name(), Job.학생.name(), "Java");
        User findUser2 = userService.join(userJoinForm3);

        PostSaveForm postSaveForm9 = new PostSaveForm(tags,"boo1", "boo1","boo1", Type.SEE.name(),"Java",1);
        PostSaveForm postSaveForm10 = new PostSaveForm(tags,"boo2", "boo2","boo2", Type.SEE.name(),"Java",3);
        PostSaveForm postSaveForm11 = new PostSaveForm(tags,"boo3", "boo3","boo3", Type.SEE.name(),"Java",2);
        postServiceImplV1.registration(findUser2.getUser_id(), postSaveForm9);
        postServiceImplV1.registration(findUser2.getUser_id(), postSaveForm10);
        postServiceImplV1.registration(findUser2.getUser_id(), postSaveForm11);

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
        tags.add("test1"); tags.add("test2");
        PostSaveForm postSaveForm = new PostSaveForm(tags,"test", "test","test",  Type.SEE.name(),"Java",4);

        //when
        Post addPost = postServiceImplV1.registration(findUser.getUser_id(), postSaveForm);

        //then
        Assertions.assertThat(addPost.getPostTagList().size()).isEqualTo(2);

        for(String tagName: tags) {
            Optional<Tag> findTag = tagRepository.findByTagName(tagName);
            Assertions.assertThat(findTag.get()).isNotNull();
            System.out.println(findTag.get());
        }
    }

    @Test
    @Transactional
    public void 게시물업데이트태그테스트() throws Exception {
        //given
        //게시물 등록1
        User findUser = userService.findUserByNickName("test2");
        List<String> tags1 = new ArrayList<>();
        tags1.add("test1"); tags1.add("test2");
        PostSaveForm postSaveForm1 = new PostSaveForm(tags1,"test1", "test1","test1",  Type.SEE.name(),"Java",4);
        Post addPost1 = postServiceImplV1.registration(findUser.getUser_id(), postSaveForm1);

        //게시물 등록2
        List<String> tags2 = new ArrayList<>();
        tags2.add("test1");
        PostSaveForm postSaveForm2 = new PostSaveForm(tags2,"test2", "test2","test2",  Type.SEE.name(),"Java",4);
        Post addPost2 = postServiceImplV1.registration(findUser.getUser_id(), postSaveForm2);

        //when
        //게시물 업데이트
        List<String> updateTags = new ArrayList<>();
        updateTags.add("test2");
        PostEditForm postEditForm = new PostEditForm(updateTags, addPost1.getTitle(), addPost1.getProblem_uri(), addPost1.getContext(),
                addPost1.getType().name(),addPost1.getLanguage(),addPost1.getLevel());

        postServiceImplV1.updatePost(addPost1.getPost_id(), postEditForm);

        //then
        Optional<Tag> findTag = tagRepository.findByTagName("test1");
        Assertions.assertThat(findTag.get().getNum()).isEqualTo(1);
    }

    @Test
    public void 게시물삭제태그테스트() throws Exception {
        //given
        //게시물 등록1
        User findUser = userService.findUserByNickName("test2");
        List<String> tags1 = new ArrayList<>();
        tags1.add("test1"); tags1.add("test2");
        PostSaveForm postSaveForm1 = new PostSaveForm(tags1,"test1", "test1","test1",  Type.SEE.name(),"Java",4);
        Post addPost1 = postServiceImplV1.registration(findUser.getUser_id(), postSaveForm1);

        //when
        postServiceImplV1.deletePost(addPost1.getPost_id());

        //then
        Optional<Tag> test1 = tagRepository.findByTagName("test1");
        Optional<Tag> test2 = tagRepository.findByTagName("test2");
        Assertions.assertThat(test1.get().getNum()).isEqualTo(0);
        Assertions.assertThat(test2.get().getNum()).isEqualTo(0);
    }

    @Test
    public void 게시물조회수테스트_해당게시물ID가토큰에존재하는경우() throws Exception {
        //given
        //게시물 등록1
        User findUser = userService.findUserByNickName("test2");
        List<String> tags1 = new ArrayList<>();
        tags1.add("test1"); tags1.add("test2");
        PostSaveForm postSaveForm1 = new PostSaveForm(tags1,"test1", "test1","test1",  Type.SEE.name(),"Java",4);
        Post addPost1 = postServiceImplV1.registration(findUser.getUser_id(), postSaveForm1);

        Cookie cookie = new Cookie("postView", "["+ addPost1.getPost_id() + "]");
        response = new MockHttpServletResponse();

        //when
        postServiceImplV1.readPost(addPost1.getPost_id(), cookie, response); //게시물 읽기

        //then
        //조회수가 증가되면 안됌!!!!!!
        Post findPost = postRepository.findPostByPostId(addPost1.getPost_id());
        Assertions.assertThat(findPost.getCountView()).isEqualTo(0L);
    }

    @Test
    public void 게시물조회수테스트_해당게시물ID가토큰에존재하지않는경우() throws Exception {
        //given
        //게시물 등록1
        User findUser = userService.findUserByNickName("test2");
        List<String> tags1 = new ArrayList<>();
        tags1.add("test1"); tags1.add("test2");
        PostSaveForm postSaveForm1 = new PostSaveForm(tags1,"test1", "test1","test1",  Type.SEE.name(),"Java",4);
        Post addPost1 = postServiceImplV1.registration(findUser.getUser_id(), postSaveForm1);

        Cookie cookie = null;
        response = new MockHttpServletResponse();

        //when
        postServiceImplV1.readPost(addPost1.getPost_id(), cookie, response); //게시물 읽기

        //then
        //조회수가 증가되어야 함!!!!!!
        Post findPost = postRepository.findPostByPostId(addPost1.getPost_id());
        Assertions.assertThat(findPost.getCountView()).isEqualTo(1L);
    }


    @Test
    @Transactional
    public void 모든유저게시물에대해서필터링조회최신순정렬무한스크롤테스트() throws Exception {

        //when
        PostSearchFormAboutAllUser form = new PostSearchFormAboutAllUser("Java", "koo", OrderKey.latest.name(), null, null,null,null);
        Slice<Post> findPosts = postServiceImplV1.findPostsAboutAllUser(form);

        //then
        boolean hasNext = findPosts.hasNext();
        List<Post> content = findPosts.getContent();

        Assertions.assertThat(hasNext).isFalse();
    }
//    @Test
//    @Transactional
//    public void 모든유저게시물에대해서필터링조회최신순정렬무한스크롤중간페이지() throws Exception {
//
//        //when
//        SearchFormAboutAllUserPost form = new SearchFormAboutAllUserPost("Java", "koo", "latest", 6L, null,null,null);
//        Slice<Post> findPosts = postService.findPostsAboutAllUser(form);
//
//        //then
//        boolean hasNext = findPosts.hasNext();
//        List<Post> content = findPosts.getContent();
//
//        Assertions.assertThat(hasNext).isTrue();
//        Assertions.assertThat(content.size()).isEqualTo(3);
//
//    }
//    @Test
//    @Transactional
//    public void 모든유저게시물에대해서필터링조회최신순정렬무한스크롤마지막페이지() throws Exception {
//
//        //when
//        SearchFormAboutAllUserPost form = new SearchFormAboutAllUserPost("Java", "koo", "latest", 3L, null,null,null);
//        Slice<Post> findPosts = postService.findPostsAboutAllUser(form);
//
//        //then
//        boolean hasNext = findPosts.hasNext();
//        List<Post> content = findPosts.getContent();
//
//        Assertions.assertThat(hasNext).isFalse();
//        Assertions.assertThat(content.size()).isEqualTo(1);
//    }

    @Test
    @Transactional
    public void 모든유저게시물에대해서필터링조회좋아요순정렬무한스크롤테스트() throws Exception {

        //when
        PostSearchFormAboutAllUser form = new PostSearchFormAboutAllUser("Java", "koo", OrderKey.likeNum.name(), null, null,null,null);
        Slice<Post> findPosts = postServiceImplV1.findPostsAboutAllUser(form);

        //then
        boolean hasNext = findPosts.hasNext();
        List<Post> content = findPosts.getContent();

        Assertions.assertThat(hasNext).isFalse();
    }
//    @Test
//    @Transactional
//    public void 모든유저게시물에대해서필터링조회좋아요순정렬무한스크롤중간페이지() throws Exception {
//
//        //when
//        SearchFormAboutAllUserPost form = new SearchFormAboutAllUserPost("Java", "koo", "likeNum", 6L, null,6,null);
//        Slice<Post> findPosts = postService.findPostsAboutAllUser(form);
//
//        //then
//        boolean hasNext = findPosts.hasNext();
//        List<Post> content = findPosts.getContent();
//
//        Assertions.assertThat(hasNext).isTrue();
//        Assertions.assertThat(content.size()).isEqualTo(3);
//    }
//    @Test
//    @Transactional
//    public void 모든유저게시물에대해서필터링조회좋아요순정렬무한스크롤마지막페이지() throws Exception {
//
//        //when
//        SearchFormAboutAllUserPost form = new SearchFormAboutAllUserPost("Java", "koo", "likeNum", 2L, null,3,null);
//        Slice<Post> findPosts = postService.findPostsAboutAllUser(form);
//
//        //then
//        boolean hasNext = findPosts.hasNext();
//        List<Post> content = findPosts.getContent();
//
//        Assertions.assertThat(hasNext).isFalse();
//        Assertions.assertThat(content.size()).isEqualTo(1);
//    }

    @Test
    @Transactional
    public void 다른유저게시물들에대해서태그게시물조회최신순순무한스크롤테스트() throws Exception {
        //given
        SearchFormAboutOtherUserPost form = SearchFormAboutOtherUserPost.builder()
                .language("Java")
                .searchTitle("boo")
                .orderKey(OrderKey.latest.name())
                .lastPostId(null)
                .lastLikeNum(null)
                .lastReplyNum(null)
                .countView(null)
                .build();
        //when
        Slice<Post> findPosts = postServiceImplV1.findPostsAboutOtherUser("test3", form);

        //then
        List<PostListDtoAboutAllUser> result = findPosts.getContent().stream()
                .map(p -> new PostListDtoAboutAllUser(p))
                .collect(Collectors.toList());

        Assertions.assertThat(result.size()).isEqualTo(3);

    }

    @Test
    @Transactional
    public void 마이페이지게시물리스트테스트_태그1() throws Exception {
        //given
        List<String> list = new ArrayList<>();
        PostSearchFormAboutSpecificUser form = PostSearchFormAboutSpecificUser.builder()
                .tags(list)
                .type("")
                .language("")
                .searchTitle("")
                .orderKey(OrderKey.latest.name())
                .lastPostId(null)
                .lastLikeNum(null)
                .lastReplyNum(null)
                .countView(null)
                .build();

        //when
        Slice<Post> findPosts = postService.findPostsAboutMyPage("test2", form);

        //then
        Assertions.assertThat(findPosts.getContent().size()).isEqualTo(8);
    }

    @Test
    @Transactional
    public void 마이페이지게시물리스트테스트_태그2() throws Exception {
        //given
        List<String> list = new ArrayList<>();
        list.add("DFS"); list.add("FFS");
        PostSearchFormAboutSpecificUser form = PostSearchFormAboutSpecificUser.builder()
                .tags(list)
                .type("")
                .language("")
                .searchTitle("")
                .orderKey(OrderKey.latest.name())
                .lastPostId(null)
                .lastLikeNum(null)
                .lastReplyNum(null)
                .countView(null)
                .build();

        //when
        Slice<Post> result = postService.findPostsAboutMyPage("test2", form);

        //then
        Assertions.assertThat(result.getContent().size()).isEqualTo(8);
    }

    @Test
    public void 마이페이지게시물리스트테스트_태그3() throws Exception {
        //given
        List<String> list = new ArrayList<>();
        list.add("FFS");
        PostSearchFormAboutSpecificUser form = PostSearchFormAboutSpecificUser.builder()
                .tags(list)
                .type("")
                .language("")
                .searchTitle("")
                .orderKey(OrderKey.latest.name())
                .lastPostId(null)
                .lastLikeNum(null)
                .lastReplyNum(null)
                .countView(null)
                .build();

        //when
        Slice<Post> result = postService.findPostsAboutMyPage("test2", form);

        //then
        Assertions.assertThat(result.getContent().size()).isEqualTo(0);
    }

    @Test
    public void 마이페이지게시물리스트_정렬테스트() throws Exception {
        //given
        PostSearchFormAboutAllUser form  = PostSearchFormAboutAllUser.builder()
                .language("")
                .searchTitle("")
                .orderKey("likeNum")
                .lastPostId(10L)
                .lastReplyNum(null)
                .lastLikeNum(10)
                .countView(null)
                .build();

        //when
        Slice<Post> result = postService.findPostsAboutAllUser(form);

        //then
        //Assertions.assertThat(result.getContent().size()).isEqualTo(0);
    }

}