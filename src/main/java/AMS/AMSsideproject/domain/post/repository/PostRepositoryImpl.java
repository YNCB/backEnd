package AMS.AMSsideproject.domain.post.repository;

import AMS.AMSsideproject.domain.post.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class PostRepositoryImpl implements PostRepository {

    @Autowired private EntityManager em;
    private JPAQueryFactory query;

    public PostRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public Post save(Post post) {
        em.persist(post);
        return post;
    }

    @Override
    public Post findByPostId(Long postId) {
        return em.find(Post.class, postId);
    }

    /**
     * 필터링에 따라서 게시물을 조회
     * !!!!해당 유저가 가지고있는 해시태그들은 항상 보여줘야되니깐 항상 모든 해시태그를 줘야된다!!
     */
    @Override
    public List<Post> findAllBySearchForm() {

        return null;
    }

}
