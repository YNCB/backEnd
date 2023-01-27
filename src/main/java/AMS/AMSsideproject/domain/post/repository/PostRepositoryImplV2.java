package AMS.AMSsideproject.domain.post.repository;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.QPost;
import AMS.AMSsideproject.domain.post.Type;
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

import static com.querydsl.jpa.JPAExpressions.select;

@Repository
public class PostRepositoryImplV2 implements PostRepositoryV2 {

    @Autowired
    private EntityManager em;
    private JPAQueryFactory query;

    public PostRepositoryImplV2(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    //게시물 저장
    @Override
    public Post save(Post post) {
        em.persist(post);
        return post;
    }

    //게시물 삭제
    @Override
    public void delete(Post post) {
        em.remove(post);
    }

    //특정 게시물 아이디로 검색
    @Override
    public Post findPost(Long postId) {
        return em.find(Post.class, postId);
    }

    //모든 유저에 대한 게시물 리스트 조회
    @Override
    public Slice<Post> findPostsAll(String lang, String title, Pageable pageable, BooleanBuilder builder) {
        JPAQuery<Post> query = this.query.select(QPost.post)
                .from(QPost.post)
                .join(QPost.post.user, QUser.user)
                .fetchJoin() //"패치조인"으로 성능 최적화(user 쿼리문은 안나감-프록시 초기화할때)
                .where(
                        LanguageEq(lang),
                        TitleContains(title),
                        builder //NoOffset 사용하기
                );
        
        //동적 정렬
        for(Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(QPost.post.getType(), QPost.post.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC: Order.DESC, pathBuilder.get(o.getProperty())));
        }

        //무한 스크롤 페이징 처리
        List<Post> posts = query
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkEndPage(pageable, posts);
    }

    //마이 게시물 리스트 조회
    public Slice<Post> findPostMy(String nickname, List<String> tags, String type, String lang, String title ,
                                  Pageable pageable, BooleanBuilder builder){

        JPAQuery<Post> query = this.query.select(QPost.post)
                .from(QPost.post)
                .join(QPost.post.user, QUser.user)
                .where(
                        QPost.post.post_id.in(
                                select(QPost.post.post_id)
                                        .from(QPostTag.postTag)
                                        .join(QPostTag.postTag.post, QPost.post)
                                        .join(QPostTag.postTag.tag, QTag.tag)
                                        .where(TagsIn(tags))
                                        .groupBy(QPost.post.post_id)
                        ),
                        LanguageEq(lang),
                        NicknameEq(nickname),
                        TypeEq(type),
                        TitleContains(title),
                        builder
                );

        //동적 정렬
        for(Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(QPost.post.getType(), QPost.post.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC: Order.DESC, pathBuilder.get(o.getProperty())));
        }

        //무한 스크롤 페이징 처리
        List<Post> posts = query
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return checkEndPage(pageable, posts);
    }
    
    //게스트 게시물 리스트 조회
    @Override
    public Slice<Post> findPostsSpecific(String nickname, String lang, String title,
                                         Pageable pageable, BooleanBuilder builder) {

        JPAQuery<Post> query = this.query.select(QPost.post)
                .from(QPost.post)
                .join(QPost.post.user, QUser.user)
                .where(
                        LanguageEq(lang),
                        NicknameEq(nickname),
                        TitleContains(title),
                        builder
                );

        //동적 정렬
        for(Sort.Order o : pageable.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(QPost.post.getType(), QPost.post.getMetadata());
            query.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC: Order.DESC, pathBuilder.get(o.getProperty())));
        }

        //무한 스크롤 페이징 처리
        List<Post> posts = query
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return checkEndPage(pageable, posts);

    }

    //게시물 조회수를 "지연감지" 없이 직접 업데이트 쿼리문 사용시
    public void updateViewCount(Long postId) {
        this.query.update(QPost.post)
                .set(QPost.post.countView, QPost.post.countView.add(1))
                .where(postIdEqAboutPost(postId))
                .execute();
    }

    /*****************************************************************************************************************************************/

    private BooleanExpression postIdEqAboutPost(Long postId) {
        if(postId == null)
            return null;
        return QPost.post.post_id.eq(postId);
    }

    private BooleanExpression TagsIn(List<String> tags) {
        if(tags.isEmpty())
            return null;
        return QTag.tag.name.in(tags);
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

        if(results.size() > pageable.getPageSize()) { //다음 게시물이 있는 경우
            hasNext = true;
            results.remove(pageable.getPageSize()); //한개더 가져왔으니 더 가져온 데이터 삭제
        }
        return new SliceImpl<>(results, pageable, hasNext);
    }

    private BooleanExpression NicknameEq(String nickname) {
        if(!StringUtils.hasText(nickname))
            return null;
        return QPost.post.user.nickname.eq(nickname);
    }

    private BooleanExpression TypeEq(String type) {
        if(!StringUtils.hasText(type))
            return null;
        return QPost.post.type.eq(Type.valueOf(type));
    }
}
