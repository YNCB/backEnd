package AMS.AMSsideproject.domain.post.repository;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.QPost;
import AMS.AMSsideproject.domain.tag.Tag.QTag;
import AMS.AMSsideproject.domain.tag.postTag.QPostTag;
import AMS.AMSsideproject.domain.user.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {

    @Autowired private EntityManager em;
    private JPAQueryFactory query;

    public PostRepositoryImpl(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    //게시물 저장
    @Override
    public Post save(Post post) {
        em.persist(post);
        return post;
    }

    //특정 게시물 아이디로 검색
    @Override
    public Post findPostByPostId(Long postId) {
        return em.find(Post.class, postId);
    }

    //모든 유저에 대한 게시물 조회
    @Override
    public Slice<Post> findPostsBySearchFormAboutAllUser(String lang, String title, Pageable pageable, BooleanBuilder builder) {
        JPAQuery<Post> query = this.query.select(QPost.post
//                Projections.fields(PostListDto.class,
//                        QPost.post.post_id,
//                        QPost.post.title,
//                        QUser.user.nickname,
//                        QPost.post.language,
//                        QPost.post.type,
//                        QPost.post.likeNum,
//                        QPost.post.replyNum)
                        )
                .from(QPost.post)
                .where(
                        LanguageEq(lang),
                        TitleContains(title),
                        builder //NoOffset 사용하기
                )
                .join(QPost.post.user, QUser.user)
                .fetchJoin();
        /**
         * 작성자 닉네임을 항상 가져와야 되기 때문에 "fetchJoin" 으로 "N+1" 문제 해결
         */

        //동적 정렬
        for(Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(QPost.post.getType(), QPost.post.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC: Order.DESC, pathBuilder.get(o.getProperty())));
        }

        List<Post> posts = query
                .limit(pageable.getPageSize() + 1)
                .fetch();



        //oneToMany (tags) 따로 조회에서 넣기"1+N" 문제를 방지하기 위해 "Batch fetch size" 사용
        /**
         * 문제점
         * 1) 아래 방식은 단건조회에서 단건조회에 있는 oneToMany를 조회하는 방식이다
         *   대량의 데이터에 대해서는 성능이 너무 나오지 않는다. -> 1+N 문제 발생
         * 2) "checkEndPage" 를 통해 추가로 가져온 데이터 하나는 삭제하는데 이 데이터는 oneToMany 인 데이터는 가져올 필요없지!! -> 쓸데없이 쿼리문 하나만 더 나가니?!
         *
         * V3.1, V4, V5 버전 보기
         * : V3.1 버전에서 "Batch~" 적용가능 -> JPA 에서 DTO로 변환 x => "Bath~" 는 프록시가 초기화 될때 해당 프록시가 있는 테이블의 데이터를 일정개수 바로 올리는것
         * : V4 버전에는 "Bath~" 적용안됨 -> JPA에서 DTO변환 (시간절약) => 단건 조회에서 사용 (왜냐하면 루트 쿼리의 결과 개수만큼 oneToMany 쿼리가 나가니 -> "1+N" 쿼리)
         * : V5 ~~~~  => 대량 데이터 조회에서 ("1+1" 쿼리)
         */
//        posts.stream()
//                .forEach( p -> {
//                    List<String> postTags = findPostTags(p.getPost_id());
//                    p.setTags(postTags);
//                });

        return checkEndPage(pageable, posts);
    }



    private BooleanExpression LanguageEq(String language) {
        if(!StringUtils.hasText(language))
            return null;
        return QPost.post.language.eq(language);
    }
    private BooleanExpression TitleContains(String title) {
        if(!StringUtils.hasText(title))
            return null;
        return QPost.post.title.contains(title); // = ~.like("%"+title+"%")
    }
    private Slice<Post> checkEndPage(Pageable pageable, List<Post> results) {
        boolean hasNext = false;

        if(results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize()); //한개더 가져왔으니 더 가져온 데이터 삭제
        }
        return new SliceImpl<>(results, pageable, hasNext);
    }



    private List<String> findPostTags(Long postId) {
        return query.select(QTag.tag.name)
                .from(QPostTag.postTag)
                .join(QPostTag.postTag.tag, QTag.tag)
                .where(postIdEq(postId))
                .fetch();
    }
    private BooleanExpression postIdEq(Long postId) {
        if(postId==null)
            return null;
        return QPostTag.postTag.post.post_id.eq(postId);
    }

}
