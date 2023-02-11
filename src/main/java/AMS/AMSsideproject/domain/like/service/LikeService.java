package AMS.AMSsideproject.domain.like.service;

import AMS.AMSsideproject.domain.like.Like;
import AMS.AMSsideproject.domain.like.repository.LikeRepository;
import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.repository.PostRepositoryV1;
import AMS.AMSsideproject.domain.user.User;
import AMS.AMSsideproject.domain.user.repository.UserRepository;
import AMS.AMSsideproject.web.exception.NotExistingUser;
import AMS.AMSsideproject.web.responseDto.like.LikeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final PostRepositoryV1 postRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;

    /** 양방향 vs 단방향
     * 1. post 쿼리1, post.likes 프록시 쿼리 1, ((추가 쿼리1 + user 쿼리1) or 삭제 쿼리 1), post.likeNUm 업데이트 쿼리 -> "양방향"
     * 2. like 쿼리1 , (추가 쿼리1 or 삭제 쿼리1), post 쿼리1, post.likeNum 업데이트 쿼리1 -> "단방향"
     */

    //좋아요 클릭(추가, 삭제) 요청 왔을때 내부적으로 판단해서 해당 "post" "List<Like>"에 추가 삭제 기능
//    @Transactional
//    public LikeDto like_v1(Long postId, Long userId) {
//
//        Post findPost = postRepository.findPostByPostId(postId);
//        //여기서 프록시 초기화 되면서 게시물에 있는 좋아요 객체들을 하나의 쿼리로 가져옴
//        List<Like> likes = findPost.getLikes();
//
//        //좋아요 누른 게시물인지 판별
//        boolean check = false;
//        Like deleteLike = null;
//        for(Like like : likes) {
//            if(like.getUser().getUser_id() == userId) { //userId를 꺼낼때는 N+1문제없음 user의 다른 속성들을 가져올때 쿼리문 발생.
//                deleteLike = like;
//                check = true;
//                break;
//            }
//        }
//
//        if(check == true) { //취소되야되는 경우
//            findPost.getLikes().remove(deleteLike);
//            check = false;
//
//        }else { //추가되야 되는 경우
//            User findUser = userRepository.findByUserId(userId).get();
//
//            Like createLike = Like.create(findPost, findUser);
//            findPost.addLike(createLike);
//            check = true;
//        }
//
//        findPost.setLikeNum();
//        return new LikeDto(check, findPost.getLikeNum());
//    }

    //양방향으로 다시 바꿈
    @Transactional
    public LikeDto like_v2(Long postId, Long userId) {

        Post findPost = postRepository.findPostByPostId(postId);
        Optional<Like> likeCheck = likeRepository.findLikeCheck(postId, userId);

        Boolean check = false;
        if(likeCheck.isEmpty()){ //추가되어야 되는 경우

            Optional<User> findUser = userRepository.findByUserId(userId);
            findUser.orElseThrow( () -> new NotExistingUser("존재하지 않은 사용자 입니다."));

            Like newLike = Like.create(findPost, findUser.get());
            likeRepository.save2(newLike);
            //likeRepository.save(postId,userId);

            findPost.addLikeNum();
            check = true;

        }else { //삭제되어야 되는 경우
            likeRepository.delete2(likeCheck.get());
            //likeRepository.delete(postId, userId);

            findPost.subLikeNum();
        }

        return new LikeDto(check, findPost.getLikeNum());
    }

    //게시물 상세조회에서 게시물 좋아요 누른 사용자 인지 판별
    public Boolean checkExisting(Long postId, Long userId) {

        Optional<Like> existing = likeRepository.findLikeCheck(postId, userId);

        if(existing.isEmpty())
            return false;

        return true;
    }

}
