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

    //댓글 조회
    public Reply findReply(Long replyId) {
        return em.find(Reply.class, replyId);
    }

    //alias 사용
    QReply r1 = new QReply("r1");
    QReply r2 = new QReply("r2");
    //댓글 리스트 조회
    public List<Reply> findReplies(Long postId) {
        return query.select(r1)
                .from(r1)
                .leftJoin(r1.parent  , r2)
                .fetchJoin()  //"N+1"문제 해결
                .where(postIdEq(postId))
                .orderBy(r2.reply_id.asc().nullsFirst(), r1.redate.asc()) //루트 댓글을 처음에 배치하기 위해
                .fetch();
    }

    //댓글 삭제
    public void delete(Reply reply) {
        em.remove(reply);
    }

    private BooleanExpression postIdEq(Long postId) {
        return (postId==null? null: r1.post.post_id.eq(postId));
    }

}
