package AMS.AMSsideproject.domain.post.repository;

import AMS.AMSsideproject.domain.post.Post;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PostRepository {

    //게시물 저장
    public Post save(Post post);

    //게시물 삭제
    public void delete(Post post);

    //특정 게시물 검색
    public Post findPostByPostId(Long postId);

    //모든 유저의 전체 게시물 검색(필터링 조건)
    public Slice<Post> findPostsByAllUser(String lang, String title, Pageable pageable, BooleanBuilder builder);

    //다른 유저의 전체 게시물 검색(필터링 조건)
    public Slice<Post> findPostsByOtherUser(String nickname, String language, String title , Pageable pageable, BooleanBuilder builder);

    //자신의 전체 게시물 검색(필터링 조건)
    public Slice<Post> findPostsByOneSelf(String nickname, List<String> tags, String type, String language, String title , Pageable pageable, BooleanBuilder builder);


}
