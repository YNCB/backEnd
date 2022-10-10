package AMS.AMSsideproject.domain.like.repository;

import AMS.AMSsideproject.domain.like.Like;
import AMS.AMSsideproject.domain.like.QLike;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
    public Optional<Like> findLike(Long postId, Long userId) {
        Like like = query.select(QLike.like)
                .from(QLike.like)
                .where(
                        postIdEq(postId),
                        userIdEq(userId))
                .fetchFirst(); //어차피 한개밖에 없음, 게시물이 한개에 대해서이니

        return Optional.ofNullable(like);
    }

    private BooleanExpression postIdEq(Long postId) {
        if(Objects.isNull(postId))
            return null;
        return QLike.like.post.post_id.eq(postId);
    }

    private BooleanExpression userIdEq(Long userId) {
        if(Objects.isNull(userId))
            return null;
        return QLike.like.user_id.eq(userId);
    }

}
