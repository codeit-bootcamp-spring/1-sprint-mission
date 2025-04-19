package com.sprint.mission.entity.addOn;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import com.sprint.mission.entity.UserStatus;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserStatus is a Querydsl query type for UserStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserStatus extends EntityPathBase<UserStatus> {

    private static final long serialVersionUID = 267956621L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserStatus userStatus = new QUserStatus("userStatus");

    public final com.sprint.mission.entity.main.QBaseUpdatableEntity _super = new com.sprint.mission.entity.main.QBaseUpdatableEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    //inherited
    public final ComparablePath<java.util.UUID> id = _super.id;

    public final DateTimePath<java.time.Instant> lastActiveAt = createDateTime("lastActiveAt", java.time.Instant.class);

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    public final com.sprint.mission.entity.main.QUser user;

    public QUserStatus(String variable) {
        this(UserStatus.class, forVariable(variable), INITS);
    }

    public QUserStatus(Path<? extends UserStatus> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserStatus(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserStatus(PathMetadata metadata, PathInits inits) {
        this(UserStatus.class, metadata, inits);
    }

    public QUserStatus(Class<? extends UserStatus> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.sprint.mission.entity.main.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

