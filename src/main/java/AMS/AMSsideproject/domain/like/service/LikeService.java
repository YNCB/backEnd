package AMS.AMSsideproject.domain.like.service;

import AMS.AMSsideproject.domain.like.Like;
import AMS.AMSsideproject.domain.like.repository.LikeRepository;
import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.repository.PostRepository;
import AMS.AMSsideproject.web.responseDto.like.LikeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    //좋아요 클릭(추가, 삭제) 요청 왔을때 내부적으로 판단해서 해당 "post" "List<Like>"에 추가 삭제 기능
    @Transactional
    public LikeDto like(Long postId, Long userId) {

        //1. 프록시 초기화 방법이 빠르나
        //2. 그냥 쿼리문 으로 하는게 빠르나...움

        Post findPost = postRepository.findPostByPostId(postId);
        //여기서 프록시 초기화 되면서 게시물에 있는 좋아요 객체들을 하나의 쿼리로 가져옴
        List<Like> likes = findPost.getLikes();

        //좋아요 누른 게시물인지 판별
        boolean check = false;
        Like deleteLike = null;
        for(Like like : likes) {
            if(like.getUser_id() == userId) {
                deleteLike = like;
                check = true;
                break;
            }
        }

        if(check == true) { //취소되야되는 경우
            findPost.getLikes().remove(deleteLike);
            check = false;
        }else { //추가되야 되는 경우
            Like createLike = Like.create(findPost, userId);
            findPost.getLikes().add(createLike);
            check = true;
        }

        return new LikeDto(check, findPost.getLikes().size());
    }


    //게시물 상세조회에서 게시물 좋아요 누른 사용자 인지 판별
    public Boolean checkExisting(Long postId, Long userId) {

        Optional<Like> existing = likeRepository.findLike(postId, userId);
        if(existing.isEmpty())
            return false;
        return true;
    }

}
