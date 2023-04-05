package xyz.spoonmap.server.spoon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSpoon is a Querydsl query type for Spoon
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSpoon extends EntityPathBase<Spoon> {

    private static final long serialVersionUID = 294183520L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSpoon spoon = new QSpoon("spoon");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final QSpoon_Pk id;

    public final xyz.spoonmap.server.member.entity.QMember member;

    public final xyz.spoonmap.server.post.entity.QPost post;

    public QSpoon(String variable) {
        this(Spoon.class, forVariable(variable), INITS);
    }

    public QSpoon(Path<? extends Spoon> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSpoon(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSpoon(PathMetadata metadata, PathInits inits) {
        this(Spoon.class, metadata, inits);
    }

    public QSpoon(Class<? extends Spoon> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QSpoon_Pk(forProperty("id")) : null;
        this.member = inits.isInitialized("member") ? new xyz.spoonmap.server.member.entity.QMember(forProperty("member")) : null;
        this.post = inits.isInitialized("post") ? new xyz.spoonmap.server.post.entity.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

