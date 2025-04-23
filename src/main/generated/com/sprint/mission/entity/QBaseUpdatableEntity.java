package com.sprint.mission.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBaseUpdatableEntity is a Querydsl query type for BaseUpdatableEntity
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QBaseUpdatableEntity extends EntityPathBase<BaseUpdatableEntity> {

    private static final long serialVersionUID = -581477882L;

    public static final QBaseUpdatableEntity baseUpdatableEntity = new QBaseUpdatableEntity("baseUpdatableEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    //inherited
    public final ComparablePath<java.util.UUID> id = _super.id;

    public final DateTimePath<java.time.Instant> updatedAt = createDateTime("updatedAt", java.time.Instant.class);

    public QBaseUpdatableEntity(String variable) {
        super(BaseUpdatableEntity.class, forVariable(variable));
    }

    public QBaseUpdatableEntity(Path<? extends BaseUpdatableEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBaseUpdatableEntity(PathMetadata metadata) {
        super(BaseUpdatableEntity.class, metadata);
    }

}

