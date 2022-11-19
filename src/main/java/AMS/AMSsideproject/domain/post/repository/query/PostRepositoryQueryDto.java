package AMS.AMSsideproject.domain.post.repository.query;

import AMS.AMSsideproject.domain.like.QLike;
import AMS.AMSsideproject.domain.post.QPost;
import AMS.AMSsideproject.domain.post.repository.query.form.LikeDto;
import AMS.AMSsideproject.domain.tag.Tag.QTag;
import AMS.AMSsideproject.domain.tag.postTag.QPostTag;
import AMS.AMSsideproject.domain.user.QUser;
import AMS.AMSsideproject.web.responseDto.post.PostDto;
import AMS.AMSsideproject.web.responseDto.post.PostEditDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class PostRepositoryQueryDto {

    @Autowired
    private EntityManager em;
    private JPAQueryFactory query;

    public PostRepositoryQueryDto(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    //단건 조회에 대해서 Dto로 성능 튜닝(기존과 쿼리 수는 동일하나 dto로 변환하여 조금 줄이기위해서)
    public PostDto findQueryPostDtoByPostId(Long postId) {

        PostDto findPostDto = query.select(Projections.constructor(PostDto.class,
                        QPost.post.post_id,QPost.post.title, QUser.user.nickname, QPost.post.redate, QPost.post.likeNum,
                        QPost.post.language,QPost.post.type, QPost.post.level, QPost.post.context, QPost.post.replyNum
                ))
                .from(QPost.post)
                .where(postIdEqAboutPost(postId))
                .join(QPost.post.user, QUser.user)
                .fetchFirst(); //일치하는 게시물은 반드시 한개

        findPostDto.setTags(findPostTags(postId)); //tags 초기화!!!
        //findPostDto.setLikes(findLikes(postId)); //likes 초기화!!!

        return findPostDto;
    }

    //단건 조회에서 사용하는 법(게시물 한개) - 게시물 태그 검색
    private List<String> findPostTags(Long postId) {
        return query.select(QTag.tag.name)
                .from(QPostTag.postTag)
                .join(QPostTag.postTag.tag, QTag.tag)
                .where(postIdEqAboutPostTag(postId))
                .fetch();
    }

    //단건 조회(게시물 한개) - 게시물 좋아요 검색
//    private List<LikeDto> findLikes(Long postId) {
//        return query.select(Projections.constructor(LikeDto.class,
//                        QUser.user.user_id, QUser.user.nickname))
//                .from(QLike.like)
//                .join(QLike.like.user, QUser.user)
//                .where(postIdEqAboutLike(postId))
//                .orderBy(QLike.like.redate.desc()) //내림차순 정렬
//                .fetch();
//    }

    private BooleanExpression postIdEqAboutPostTag(Long postId) {
        if(postId==null)
            return null;
        return QPostTag.postTag.post.post_id.eq(postId);
    }

    private BooleanExpression postIdEqAboutPost(Long postId) {
        if(postId == null)
            return null;
        return QPost.post.post_id.eq(postId);
    }

    private BooleanExpression postIdEqAboutLike(Long postId) {
        if(postId ==null)
            return null;
        return QLike.like.post.post_id.eq(postId);
    }

    //게시물 수정 조회에 대해서 Dto로 성능 튜닝
    public PostEditDto findQueryPostEditDtoByPostId(Long postId){
        PostEditDto postEditDto = query.select(Projections.constructor(PostEditDto.class,
                        QPost.post.title, QPost.post.problem_uri, QPost.post.context, QPost.post.type, QPost.post.language, QPost.post.level))
                .from(QPost.post)
                .where(postIdEqAboutPost(postId))
                .fetchFirst();

        postEditDto.setTags(findPostTags(postId));
        return postEditDto;
    }
}
