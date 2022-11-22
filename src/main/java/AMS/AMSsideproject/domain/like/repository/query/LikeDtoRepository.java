package AMS.AMSsideproject.domain.like.repository.query;

import AMS.AMSsideproject.domain.like.Like;
import AMS.AMSsideproject.domain.like.QLike;
import AMS.AMSsideproject.domain.post.QPost;
import AMS.AMSsideproject.domain.user.QUser;
import AMS.AMSsideproject.web.responseDto.like.LikesDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

@Repository
public class LikeDtoRepository {

    @Autowired
    private EntityManager em;
    private JPAQueryFactory query;

    public LikeDtoRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    /**
     * 1. 프록시 초기화 하는거는 쿼리문 2개?!
     * 2. 그냥 바로 sql문으로 쿼리문 1개?!
     *
     * 잠시만 "like" 테이블에 "user_id"를 뒀는데 이거를 "user"테이블이랑 "1:1"연결을 해놔야 되나?!!!!!!!!!!!???
     * 근데 그렇게 되면 "likeservice" 에서 findpost 에 userId 가져올때 "1+N"문제 있는뎅.....
     * 이거 "객체 연결 테이블"표시 안하고 그냥 임의로 "컬럼명"으로 내가 조인해서 사용해도 좋나?!!!!!!!!!!!!!!!!!!
     */
    //게시물 좋아요 리스트 검색(Dto로 성능 향상)
    public List<String> findLikes(Long postId) {

        List<String> nicknames = query.select(
                //Projections.constructor(LikesDto.class, QUser.user.user_id, QUser.user.nickname)
                QUser.user.nickname
                )
                .from(QLike.like)
                .where(postIdEq(postId))
                .join(QLike.like.post, QPost.post)
                .join(QLike.like.user, QUser.user)
                .fetch();

        return nicknames;
    }

    private BooleanExpression postIdEq(Long postId) {
        if(Objects.isNull(postId))
            return null;
        return QLike.like.post.post_id.eq(postId);
    }

}
