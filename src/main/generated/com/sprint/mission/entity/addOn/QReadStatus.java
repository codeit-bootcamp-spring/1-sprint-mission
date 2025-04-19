package com.sprint.mission.entity.addOn;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReadStatus is a Querydsl query type for ReadStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReadStatus extends EntityPathBase<ReadStatus> {

    private static final long serialVersionUID = -1824367848L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReadStatus readStatus = new QReadStatus("readStatus");

    public final com.sprint.mission.entity.main.QBaseUpdatableEntity _super = new com.sprint.mission.entity.main.QBaseUpdatableEntity(this);

    public final com.sprint.mission.entity.main.QChannel channel;

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    //inherited
    public final ComparablePath<java.util.UUID> id = _super.id;

    public final DateTimePath<java.time.Instant> lastReadAt = createDateTime("lastReadAt", java.time.Instant.class);

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    public final com.sprint.mission.entity.main.QUser user;

    public QReadStatus(String variable) {
        this(ReadStatus.class, forVariable(variable), INITS);
    }

    public QReadStatus(Path<? extends ReadStatus> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReadStatus(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReadStatus(PathMetadata metadata, PathInits inits) {
        this(ReadStatus.class, metadata, inits);
    }

    public QReadStatus(Class<? extends ReadStatus> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.channel = inits.isInitialized("channel") ? new com.sprint.mission.entity.main.QChannel(forProperty("channel")) : null;
        this.user = inits.isInitialized("user") ? new com.sprint.mission.entity.main.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

