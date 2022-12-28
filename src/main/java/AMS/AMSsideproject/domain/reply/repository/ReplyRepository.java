package AMS.AMSsideproject.domain.reply.repository;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.reply.QReply;
import AMS.AMSsideproject.domain.reply.Reply;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class ReplyRepository {

    private EntityManager em;
    private JPAQueryFactory query;

    @Autowired
    public ReplyRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    //댓글 저장
    public void save(Reply reply){
        em.persist(reply);
    }

    //댓글 리스트 조회
    public List<Reply> findReplies(Long postId) {
        return query.select(QReply.reply)
                .from(QReply.reply)
                .leftJoin(QReply.reply, QReply.reply.parent)
                .fetchJoin() //"N+1"문제 해결
                .where(postIdEq(postId))
                .orderBy(QReply.reply.parent.reply_id.asc().nullsFirst(), QReply.reply.redate.asc())
                .fetch();
    }

    private BooleanExpression postIdEq(Long postId) {
        return (postId==null? null: QReply.reply.post.post_id.eq(postId));
    }

}
