package AMS.AMSsideproject.domain.post.repository;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.repository.form.SearchFormAboutSpecificUser;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepository {

    //게시물 저장
    public Post save(Post post);

    //특정 게시물 검색
    public Post findPostByPostId(Long postId);

    //모든 유저의 전체 게시물 검색(필터링 조건)
    public Slice<Post> findPostsByAllUser(String lang, String title, Pageable pageable, BooleanBuilder builder);

    //특정 유저의 전체 게시물 검색(필터링 조건)
    public Slice<Post> findPostsBySpecificUser(String nickname, SearchFormAboutSpecificUser form , Pageable pageable, BooleanBuilder builder);

}
