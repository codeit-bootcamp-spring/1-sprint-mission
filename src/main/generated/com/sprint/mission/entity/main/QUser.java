package com.sprint.mission.entity.main;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import com.sprint.mission.entity.ReadStatus;
import com.sprint.mission.entity.User;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -739401794L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final QBaseUpdatableEntity _super = new QBaseUpdatableEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    //inherited
    public final ComparablePath<java.util.UUID> id = _super.id;

    public final StringPath password = createString("password");

    public final com.sprint.mission.entity.addOn.QBinaryContent profile;

    public final ListPath<ReadStatus, com.sprint.mission.entity.addOn.QReadStatus> readStatus = this.<ReadStatus, com.sprint.mission.entity.addOn.QReadStatus>createList("readStatus", ReadStatus.class, com.sprint.mission.entity.addOn.QReadStatus.class, PathInits.DIRECT2);

    public final com.sprint.mission.entity.addOn.QUserStatus status;

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    public final StringPath username = createString("username");

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.profile = inits.isInitialized("profile") ? new com.sprint.mission.entity.addOn.QBinaryContent(forProperty("profile"), inits.get("profile")) : null;
        this.status = inits.isInitialized("status") ? new com.sprint.mission.entity.addOn.QUserStatus(forProperty("status"), inits.get("status")) : null;
    }

}

