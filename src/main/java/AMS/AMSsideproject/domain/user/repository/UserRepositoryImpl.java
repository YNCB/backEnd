package AMS.AMSsideproject.domain.user.repository;

import AMS.AMSsideproject.domain.user.QUser;
import AMS.AMSsideproject.domain.user.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository{

    @Autowired
    private EntityManager em;
    private JPAQueryFactory query;

    public UserRepositoryImpl(EntityManager em) {
        query = new JPAQueryFactory(em);
    }

    @Override
    public User save(User user) {
        em.persist(user);
        return user;
    }

    @Override
    public User findByUserId(Long userId) {
         return em.find(User.class, userId);
    }

    @Override
    public Optional<User> findBySocialId(String socialId) {
        return query.select(QUser.user)
                .from(QUser.user)
                .where(socialIdEq(socialId))
                .fetch()
                .stream().findFirst(); //어차피 social id 별로 한명의 사용자 밖에 없을테니
    }
    private BooleanExpression socialIdEq(String socialId) {
        if (!StringUtils.hasText(socialId))
            return null;
        return QUser.user.social_id.eq(socialId);
    }

    @Override
    public Optional<User> findByNickName(String nickName) {
        return query.select(QUser.user)
                .from(QUser.user)
                .where(nickNameEq(nickName))
                .fetch()
                .stream().findFirst(); //어차피 social id 별로 한명의 사용자 밖에 없을테니
    }
    private BooleanExpression nickNameEq(String nickName) {
        if(!StringUtils.hasText(nickName))
            return null;
        return QUser.user.nickname.eq(nickName);
    }
}
