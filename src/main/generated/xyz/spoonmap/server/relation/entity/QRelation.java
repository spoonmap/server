package xyz.spoonmap.server.relation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRelation is a Querydsl query type for Relation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRelation extends EntityPathBase<Relation> {

    private static final long serialVersionUID = -257199214L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRelation relation = new QRelation("relation");

    public final QRelation_Pk id;

    public final xyz.spoonmap.server.member.entity.QMember receiver;

    public final EnumPath<xyz.spoonmap.server.relation.enums.RelationStatus> relationStatus = createEnum("relationStatus", xyz.spoonmap.server.relation.enums.RelationStatus.class);

    public final xyz.spoonmap.server.member.entity.QMember sender;

    public QRelation(String variable) {
        this(Relation.class, forVariable(variable), INITS);
    }

    public QRelation(Path<? extends Relation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRelation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRelation(PathMetadata metadata, PathInits inits) {
        this(Relation.class, metadata, inits);
    }

    public QRelation(Class<? extends Relation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QRelation_Pk(forProperty("id")) : null;
        this.receiver = inits.isInitialized("receiver") ? new xyz.spoonmap.server.member.entity.QMember(forProperty("receiver")) : null;
        this.sender = inits.isInitialized("sender") ? new xyz.spoonmap.server.member.entity.QMember(forProperty("sender")) : null;
    }

}

