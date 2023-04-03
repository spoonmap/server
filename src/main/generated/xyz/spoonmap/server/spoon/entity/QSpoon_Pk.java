package xyz.spoonmap.server.spoon.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSpoon_Pk is a Querydsl query type for Pk
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSpoon_Pk extends BeanPath<Spoon.Pk> {

    private static final long serialVersionUID = -2006960023L;

    public static final QSpoon_Pk pk = new QSpoon_Pk("pk");

    public final NumberPath<Long> memberNo = createNumber("memberNo", Long.class);

    public final NumberPath<Long> postNo = createNumber("postNo", Long.class);

    public QSpoon_Pk(String variable) {
        super(Spoon.Pk.class, forVariable(variable));
    }

    public QSpoon_Pk(Path<? extends Spoon.Pk> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSpoon_Pk(PathMetadata metadata) {
        super(Spoon.Pk.class, metadata);
    }

}

