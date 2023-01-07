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


    //게시물 좋아요 리스트 검색(Dto로 성능 향상)
    public List<String> findLikes(Long postId) {

        List<String> nicknames = query.select(QUser.user.nickname)
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
