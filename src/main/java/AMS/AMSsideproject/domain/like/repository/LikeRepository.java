package AMS.AMSsideproject.domain.like.repository;

import AMS.AMSsideproject.domain.like.Like;
import AMS.AMSsideproject.domain.like.QLike;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.LocalDateTime;

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



    /**
     * test
     * !확인해볼것! : 왜 401에러 떴을때 내가 "시큐리티 인터셉터"에 등록한 "401"응답에러가 response로 뜨는거지!???????
     */
    public void save(Long postId, Long userId) {

        //Native query
        String sql = "INSERT INTO heart (redate, post_id, user_id) " +
                "VALUE( ?,(select p.post_id from post p where p.post_id = ?), (select u.user_id from user u where u.user_id = ?) )";

        em.createNativeQuery(sql, Like.class)
                .setParameter(1, LocalDateTime.now()).setParameter(2, postId).setParameter(3, userId)
                .executeUpdate();

    }
    public void delete(Long postId, Long userId) {

        //Native query
        String sql = "DELETE FROM heart WHERE post_id = ? AND user_id = ? ";

        em.createNativeQuery(sql, Like.class)
                .setParameter(1, postId).setParameter(2, userId)
                .executeUpdate();

//        String sql = "delete from heart h where h.post_id=:postId and h.user_id=:userId";
//
//        em.createQuery(sql).setParameter("postId", postId).setParameter("userId", userId)
//                .executeUpdate();

    }


}
