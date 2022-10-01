package AMS.AMSsideproject.domain.post.repository;

import AMS.AMSsideproject.domain.post.Post;
import AMS.AMSsideproject.domain.post.QPost;
import AMS.AMSsideproject.domain.post.repository.form.SearchFormAboutSpecificUser;
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
import java.util.ArrayList;
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
    public Slice<Post> findPostsByAllUser(String lang, String title, Pageable pageable, BooleanBuilder builder) {
        JPAQuery<Post> query = this.query.select(QPost.post)
                .from(QPost.post)
                .join(QPost.post.user, QUser.user)
                .where(
                        LanguageEq(lang),
                        TitleContains(title),
                        builder //NoOffset 사용하기
                )
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
         * -문제점-
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

    //특정 유저 페이지 게시물 조회
    public Slice<Post> findPostsBySpecificUser(String nickname, SearchFormAboutSpecificUser form , Pageable pageable, BooleanBuilder builder) {

        //태그에 속해있는 게시물들 부터 찾음
        List<Long> postIds = new ArrayList<>();
        if(!form.getTags().isEmpty())
            postIds = findPostIds(form.getTags());

        //위에 게시물들과 나머지 조건으로 게시물들 찾음
        JPAQuery<Post> query = this.query.select(QPost.post)
                .from(QPost.post)
                .join(QPost.post.user, QUser.user)
                .where(
                        PostIdIn(postIds),
                        NicknameEq(nickname),
                        TypeEq(form.getType()),
                        LanguageEq(form.getLanguage()),
                        TitleContains(form.getSearchTitle()),
                        builder
                );
        /**
         * 방법?
         * 1. post_tag 에서 검색 태그를 가지고 있는 post를 먼저 가서와서(그룹핑) 나머지 조건 걸기
         * 2. post에서 태그 조건제외하고 필터링 후 태그 프록시 초기화 할때 거르기
         */

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

    private List<Long> findPostIds(List<String> tags) {
        List<Long> postIds = query.select(QPost.post.post_id)
                .from(QPostTag.postTag)
                .join(QPostTag.postTag.post, QPost.post)
                .join(QPostTag.postTag.tag, QTag.tag)
                .where(TagsIn(tags))
                .groupBy(QPost.post.post_id)
                .fetch();
        return postIds;
    }

    private BooleanExpression PostIdIn(List<Long> postIds){
        //태그에 해당하는 게시물이 없을때는 아무것도 검색되면 안되니!!!!!
        //if(postIds.isEmpty())
        //    return null;
        return QPost.post.post_id.in(postIds);
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

        if(results.size() > pageable.getPageSize()) {
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
        return QPost.post.type.eq(type);
    }
}
