package com.sprint.mission.entity.addOn;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import com.sprint.mission.entity.BinaryContent;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBinaryContent is a Querydsl query type for BinaryContent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBinaryContent extends EntityPathBase<BinaryContent> {

    private static final long serialVersionUID = 1611209032L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBinaryContent binaryContent = new QBinaryContent("binaryContent");

    public final com.sprint.mission.entity.main.QBaseEntity _super = new com.sprint.mission.entity.main.QBaseEntity(this);

    public final StringPath contentType = createString("contentType");

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final StringPath fileName = createString("fileName");

    //inherited
    public final ComparablePath<java.util.UUID> id = _super.id;

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public final com.sprint.mission.entity.main.QUser user;

    public QBinaryContent(String variable) {
        this(BinaryContent.class, forVariable(variable), INITS);
    }

    public QBinaryContent(Path<? extends BinaryContent> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBinaryContent(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBinaryContent(PathMetadata metadata, PathInits inits) {
        this(BinaryContent.class, metadata, inits);
    }

    public QBinaryContent(Class<? extends BinaryContent> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.sprint.mission.entity.main.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

