package AMS.AMSsideproject.domain.post.repository;

import AMS.AMSsideproject.domain.post.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostRepositoryImplTest {

    @Autowired PostRepository postRepository;

    @Test
    public void 기능테스트() throws Exception {
        //given
        Long postId = 100L;
        //when
        Post findPost = postRepository.findPostByPostId(postId);
        //then
        Assertions.assertThat(findPost).isNull();
    }

}