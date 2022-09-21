package AMS.AMSsideproject.domain.tag.Tag.repository;

import AMS.AMSsideproject.domain.tag.Tag.QTag;
import AMS.AMSsideproject.domain.tag.Tag.Tag;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class TagRepository {

    @Autowired private EntityManager em;
    private JPAQueryFactory query;

    public TagRepository(EntityManager em) {
        query = new JPAQueryFactory(em);
    }

    public void save(Tag tag) {
            em.persist(tag);
    }

    public Optional<Tag> findByTagName(String tagName) {
        return query.select(QTag.tag)
                .from(QTag.tag)
                .where(tagNameEq(tagName))
                .fetch()
                .stream().findFirst();
    }
    private BooleanExpression tagNameEq(String tagName) {
        if(!StringUtils.hasText(tagName))
            return null;
        return QTag.tag.name.eq(tagName);

    }

}
