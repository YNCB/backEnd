package AMS.AMSsideproject.domain.post;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = -1445407728L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final DateTimePath<java.time.LocalDateTime> chdate = createDateTime("chdate", java.time.LocalDateTime.class);

    public final StringPath context = createString("context");

    public final NumberPath<Integer> countView = createNumber("countView", Integer.class);

    public final StringPath language = createString("language");

    public final NumberPath<Integer> level = createNumber("level", Integer.class);

    public final NumberPath<Integer> likeNum = createNumber("likeNum", Integer.class);

    public final ListPath<AMS.AMSsideproject.domain.like.Like, AMS.AMSsideproject.domain.like.QLike> likes = this.<AMS.AMSsideproject.domain.like.Like, AMS.AMSsideproject.domain.like.QLike>createList("likes", AMS.AMSsideproject.domain.like.Like.class, AMS.AMSsideproject.domain.like.QLike.class, PathInits.DIRECT2);

    public final NumberPath<Long> post_id = createNumber("post_id", Long.class);

    public final ListPath<AMS.AMSsideproject.domain.tag.postTag.PostTag, AMS.AMSsideproject.domain.tag.postTag.QPostTag> postTagList = this.<AMS.AMSsideproject.domain.tag.postTag.PostTag, AMS.AMSsideproject.domain.tag.postTag.QPostTag>createList("postTagList", AMS.AMSsideproject.domain.tag.postTag.PostTag.class, AMS.AMSsideproject.domain.tag.postTag.QPostTag.class, PathInits.DIRECT2);

    public final StringPath problem_uri = createString("problem_uri");

    public final DateTimePath<java.time.LocalDateTime> redate = createDateTime("redate", java.time.LocalDateTime.class);

    public final ListPath<AMS.AMSsideproject.domain.reply.Reply, AMS.AMSsideproject.domain.reply.QReply> replies = this.<AMS.AMSsideproject.domain.reply.Reply, AMS.AMSsideproject.domain.reply.QReply>createList("replies", AMS.AMSsideproject.domain.reply.Reply.class, AMS.AMSsideproject.domain.reply.QReply.class, PathInits.DIRECT2);

    public final NumberPath<Integer> replyNum = createNumber("replyNum", Integer.class);

    public final StringPath title = createString("title");

    public final EnumPath<Type> type = createEnum("type", Type.class);

    public final AMS.AMSsideproject.domain.user.QUser user;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new AMS.AMSsideproject.domain.user.QUser(forProperty("user")) : null;
    }

}

