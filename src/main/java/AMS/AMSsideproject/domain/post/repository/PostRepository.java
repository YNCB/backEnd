package AMS.AMSsideproject.domain.post.repository;

import AMS.AMSsideproject.domain.post.Post;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepository {

    //게시물 저장
    public Post save(Post post);

    //특정 게시물 검색
    public Post findPostByPostId(Long postId);

    //다른 사람의 전체 게시물 검색(필터링 조건)
    public Slice<Post> findPostsBySearchFormAboutAllUser(String lang, String title, Pageable pageable, BooleanBuilder builder);


}
