package com.sprint.mission.repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.dto.response.ChannelDto;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.entity.main.*;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.sprint.mission.entity.addOn.QReadStatus.readStatus;
import static com.sprint.mission.entity.main.QChannel.*;
import static com.sprint.mission.entity.main.QMessage.*;
import static com.sprint.mission.entity.main.QUser.*;

@RequiredArgsConstructor
public class CustomChannelRepositoryImpl implements CustomChannelRepository{

    private final JPAQueryFactory jpaQueryFactory;

    public List<TestDto> findAllChannelByUserId(UUID userId) {


        List<Channel> participatingPrivateChannel = jpaQueryFactory
                .select(readStatus.channel)
                .from(readStatus)
                .where(readStatus.user.id.eq(userId))
                .fetch();

        if (participatingPrivateChannel.isEmpty()) {
            return Collections.emptyList();
        }

        Map<UUID, Instant> lastMessageMap = jpaQueryFactory
                .from(message)
                .where(message.channel.in(participatingPrivateChannel))
                .transform(GroupBy.groupBy(message.channel.id)
                        .as(message.createdAt.max()));


        List<UUID> channelIds = participatingPrivateChannel.stream().map(BaseEntity::getId).toList();

        Map<UUID, List<User>> channelUserListMap = jpaQueryFactory
                .from(channel)
                .join(channel.readStatus, readStatus)
                .join(readStatus.user, user)
                .where(channel.id.in(channelIds))
                .groupBy(channel.id)
                .transform(GroupBy.groupBy(channel.id).as(GroupBy.list(user)));



    }
}

