package AMS.AMSsideproject.domain.post.repository;

import AMS.AMSsideproject.domain.post.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    //특정 게시물 검색
    public Post findByPostId(Long postId);

    //전체 게시물 검색(필터링 포함해야함)
    public List<Post> findAllBySearchForm();

    //게시물 저장
    public Post save(Post post);

}
