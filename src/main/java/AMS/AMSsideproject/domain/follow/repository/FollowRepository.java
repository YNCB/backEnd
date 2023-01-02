package AMS.AMSsideproject.domain.follow.repository;

import AMS.AMSsideproject.domain.follow.Follow;
import AMS.AMSsideproject.domain.follow.QFollow;
import AMS.AMSsideproject.domain.user.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@NoArgsConstructor
public class FollowRepository {

    private EntityManager em;
    private JPAQueryFactory query;

    private static QFollow f1 = new QFollow("f1");
    private static QUser u1 = new QUser("u1");
    private static QUser u2 = new QUser("u2");

    @Autowired
    public FollowRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    //팔로우 저장
    public void save(Follow follow) { em.persist(follow);}

    //팔로우 삭제(내가 팔로우한)
    public void delete(Follow follow) {em.remove(follow);}

    //팔로우 검색
    public Follow findFollow(Long followId) {
        return em.find(Follow.class, followId);
    }

    //팔로워 리스트 조회(내가 팔로우한)
    public List<Follow> findFollowers(Long userId) {
        return query.selectFrom(f1)
                .join(f1.user, u1)
                .join(f1.follow, u2)
                .fetchJoin() //"N+1" 문제 해결 : "u2"의 유저 닉네임만 나중에 사용될것이기 때문에
                .where(userIdEq(userId))
                .fetch();
    }

    //팔로잉 리스트 조회(나를 팔로우한)
    public List<Follow> findFollowings(Long userId) {
        return query.selectFrom(f1)
                .join(f1.user, u1)
                .fetchJoin() //"N+1" 문제 해결 : "u1"의 유저 닉네임만 나중에 사용될것이기 때문에
                .join(f1.follow, u2)
                .where(followIdEq(userId))
                .fetch();
    }

    private BooleanExpression userIdEq(Long userId) {
        return (userId==null? null: u1.user_id.eq(userId));
    }
    private  BooleanExpression followIdEq(Long followId) {
        return (followId==null? null: u2.user_id.eq(followId));
    }
}
