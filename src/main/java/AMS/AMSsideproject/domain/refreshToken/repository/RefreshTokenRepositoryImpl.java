package AMS.AMSsideproject.domain.refreshToken.repository;

import AMS.AMSsideproject.domain.refreshToken.QRefreshToken;
import AMS.AMSsideproject.domain.refreshToken.RefreshToken;
import AMS.AMSsideproject.domain.user.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

    @Autowired
    private EntityManager em;
    private JPAQueryFactory query;

    public RefreshTokenRepositoryImpl(EntityManager em) {
        query = new JPAQueryFactory(em);
    }


    @Override
    public Optional<RefreshToken> find(Long userId) {

        RefreshToken refreshToken = query.select(QRefreshToken.refreshToken)
                .from(QRefreshToken.refreshToken)
                .where(userIdEq(userId))
                .fetchFirst();

        return Optional.ofNullable(refreshToken);
    }

    @Override
    public void save(RefreshToken refreshToken) {
        em.persist(refreshToken);
    }

    @Override
    public void delete(Long userId) {
        query.delete(QRefreshToken.refreshToken)
                .where(userIdEq(userId))
                .execute();
    }

    private BooleanExpression userIdEq(Long userId) {
        return (userId==null)?null: QUser.user.user_id.eq(userId);
    }
}
