package xyz.spoonmap.server.relation.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRelation_Pk is a Querydsl query type for Pk
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QRelation_Pk extends BeanPath<Relation.Pk> {

    private static final long serialVersionUID = -81417L;

    public static final QRelation_Pk pk = new QRelation_Pk("pk");

    public final NumberPath<Long> receiverNo = createNumber("receiverNo", Long.class);

    public final NumberPath<Long> senderNo = createNumber("senderNo", Long.class);

    public QRelation_Pk(String variable) {
        super(Relation.Pk.class, forVariable(variable));
    }

    public QRelation_Pk(Path<? extends Relation.Pk> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRelation_Pk(PathMetadata metadata) {
        super(Relation.Pk.class, metadata);
    }

}

