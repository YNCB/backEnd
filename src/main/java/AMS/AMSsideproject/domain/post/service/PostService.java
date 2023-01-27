package AMS.AMSsideproject.domain.post.service;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.web.apiController.post.requestForm.*;
import AMS.AMSsideproject.web.responseDto.post.PostDto;
import org.springframework.data.domain.Slice;

import javax.servlet.http.Cookie;

public interface PostService {

    //게시물 저장
    public Post registration(Long userId, PostSaveForm form);

    //게시물 상세 검색
    public PostDto findPost(Long postId);

    //게시물 읽기 - 조회수 증가
    public Cookie readPost(Long postId , Cookie postViewCookie);

    //모든 유저의 게시물리스트 검색
    public Slice<Post> findPostsAboutAllUser(PostSearchFormAboutAllUser searchForm);

    //마이페이지의 게시물리스트 검색
    public Slice<Post> findPostsAboutMyPage(String nickname, PostSearchFormAboutSpecificUser form);

    //게시트페이지의 게시물리스트 검색
    public Slice<Post> findPostsAboutGuestPage(String nickname, PostSearchFormAboutSpecificUser form);

    //게시물 정보 업데이트
    public void updatePost(Long postId, PostEditForm postEditForm);

    //게시물 삭제
    public void deletePost(Long postId);
}
