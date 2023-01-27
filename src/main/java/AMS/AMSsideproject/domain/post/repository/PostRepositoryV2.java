package AMS.AMSsideproject.domain.post.repository;

import AMS.AMSsideproject.domain.post.Post;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PostRepositoryV2 {

    //게시물 저장
    public Post save(Post post);

    //게시물 삭제
    public void delete(Post post);

    //특정 게시물 검색
    public Post findPost(Long postId);

    //모든 게시물 리스트 조회
    public Slice<Post> findPostsAll(String lang, String title, Pageable pageable, BooleanBuilder builder);

    //마이 게시물 리스트 조회
    public Slice<Post> findPostMy(String nickname, List<String> tags, String type, String lang, String title ,
                                  Pageable pageable, BooleanBuilder builder);

    //게스트 게시물 리스트 조회
    public Slice<Post> findPostsSpecific(String nickname, String lang, String title ,
                                         Pageable pageable, BooleanBuilder builder);

    //지연감지 사용하지 않고 직접 "게시물 조회수" 업데이트
    public void updateViewCount(Long postId);
}
