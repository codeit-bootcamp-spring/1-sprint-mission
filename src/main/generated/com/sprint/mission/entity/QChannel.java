package com.sprint.mission.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChannel is a Querydsl query type for Channel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChannel extends EntityPathBase<Channel> {

    private static final long serialVersionUID = -144339679L;

    public static final QChannel channel = new QChannel("channel");

    public final QBaseUpdatableEntity _super = new QBaseUpdatableEntity(this);

    public final EnumPath<ChannelType> channelType = createEnum("channelType", ChannelType.class);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    //inherited
    public final ComparablePath<java.util.UUID> id = _super.id;

    public final StringPath name = createString("name");

    public final ListPath<ReadStatus, QReadStatus> readStatus = this.<ReadStatus, QReadStatus>createList("readStatus", ReadStatus.class, QReadStatus.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    public QChannel(String variable) {
        super(Channel.class, forVariable(variable));
    }

    public QChannel(Path<? extends Channel> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChannel(PathMetadata metadata) {
        super(Channel.class, metadata);
    }

}

