package AMS.AMSsideproject.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1186273008L;

    public static final QUser user = new QUser("user");

    public final StringPath email = createString("email");

    public final StringPath job = createString("job");

    public final StringPath main_lang = createString("main_lang");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final DateTimePath<java.time.LocalDateTime> redate = createDateTime("redate", java.time.LocalDateTime.class);

    public final StringPath role = createString("role");

    public final StringPath social_type = createString("social_type");

    public final NumberPath<Long> user_id = createNumber("user_id", Long.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

