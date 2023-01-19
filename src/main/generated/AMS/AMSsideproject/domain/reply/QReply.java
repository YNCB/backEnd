package AMS.AMSsideproject.domain.reply;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReply is a Querydsl query type for Reply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReply extends EntityPathBase<Reply> {

    private static final long serialVersionUID = 2111300872L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReply reply = new QReply("reply");

    public final DateTimePath<java.time.LocalDateTime> chdate = createDateTime("chdate", java.time.LocalDateTime.class);

    public final ListPath<Reply, QReply> children = this.<Reply, QReply>createList("children", Reply.class, QReply.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    public final QReply parent;

    public final AMS.AMSsideproject.domain.post.QPost post;

    public final DateTimePath<java.time.LocalDateTime> redate = createDateTime("redate", java.time.LocalDateTime.class);

    public final NumberPath<Long> reply_id = createNumber("reply_id", Long.class);

    public final StringPath title = createString("title");

    public final AMS.AMSsideproject.domain.user.QUser user;

    public QReply(String variable) {
        this(Reply.class, forVariable(variable), INITS);
    }

    public QReply(Path<? extends Reply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReply(PathMetadata metadata, PathInits inits) {
        this(Reply.class, metadata, inits);
    }

    public QReply(Class<? extends Reply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QReply(forProperty("parent"), inits.get("parent")) : null;
        this.post = inits.isInitialized("post") ? new AMS.AMSsideproject.domain.post.QPost(forProperty("post"), inits.get("post")) : null;
        this.user = inits.isInitialized("user") ? new AMS.AMSsideproject.domain.user.QUser(forProperty("user")) : null;
    }

}

