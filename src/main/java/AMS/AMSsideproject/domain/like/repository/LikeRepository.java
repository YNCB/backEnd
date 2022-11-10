package AMS.AMSsideproject.domain.like.repository;

import AMS.AMSsideproject.domain.like.Like;
import AMS.AMSsideproject.domain.like.QLike;
import AMS.AMSsideproject.domain.post.QPost;
import AMS.AMSsideproject.domain.user.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class LikeRepository {

    @Autowired
    private EntityManager em;
    private JPAQueryFactory query;

    public LikeRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    //게시물 아이디, 회원 아이디 요청했을때 좋아요 누른 유무 판별
    public Optional<Like> findLikeCheck(Long postId, Long userId) {
        Like like = query.select(QLike.like)
                .from(QLike.like)
                .where(
                        postIdEq(postId),
                        userIdEq(userId))
                .fetchFirst(); //어차피 한개밖에 없음, 게시물이 한개에 대해서이니

        return Optional.ofNullable(like);
    }

    //게시물 아이디, 회원 아이디 요청했을때 좋아요 리스트 반환
//    public List<Like> findLikes(Long postId, Long userId) {
//        List<Like> likes = query.select(QLike.like)
//                .from(QLike.like)
//                .join(QLike.like.user, QUser.user)
//                .join(QLike.like.post, QPost.post)
//                .where(
//                        postIdEq(postId),
//                        userIdEq(userId))
//                .fetch();
//
//        return likes;
//    }

    private BooleanExpression postIdEq(Long postId) {
        if(Objects.isNull(postId))
            return null;
        return QLike.like.post.post_id.eq(postId);
    }

    private BooleanExpression userIdEq(Long userId) {
        if(Objects.isNull(userId))
            return null;
        return QLike.like.user.user_id.eq(userId);
    }


//    //게시물 좋아요 리스트 검색
//    public List<Like> findLikes(Long posId) {
//        query.select()
//    }

}
