package com.sprint.mission.repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.dto.ChannelMapper;
import com.sprint.mission.dto.response.ChannelDto;
import com.sprint.mission.entity.main.*;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;

import static com.sprint.mission.entity.addOn.QReadStatus.readStatus;
import static com.sprint.mission.entity.main.QChannel.*;
import static com.sprint.mission.entity.main.QMessage.*;
import static com.sprint.mission.entity.main.QUser.*;

@RequiredArgsConstructor
public class CustomChannelRepositoryImpl implements CustomChannelRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ChannelMapper channelMapper;

    public List<ChannelDto> findAllPrivateChannelByUserId(UUID userId) {

        List<Channel> participatingPrivateChannel = getParticipatingPrivateChannel(userId);
        if (participatingPrivateChannel.isEmpty()) {
            return Collections.emptyList();
        }
        List<UUID> channelIds = participatingPrivateChannel.stream().map(BaseEntity::getId).toList();

        Map<UUID, List<User>> usersInChannelMap = findChannelUsersMapByChannelIds(channelIds);
        Map<UUID, Instant> lastMessageMap = getChannelLastMessagetMap(participatingPrivateChannel);

        return participatingPrivateChannel.stream().map((channel) -> {
            Instant lastMessageInChannel = lastMessageMap.get(channel.getId());
            List<User> userList = usersInChannelMap.get(channel.getId());
            return channelMapper.toDto(channel, userList, lastMessageInChannel);
        }).toList();
    }

    private Map<UUID, Instant> getChannelLastMessagetMap(List<Channel> participatingPrivateChannel) {
        return jpaQueryFactory
                .from(message)
                .where(message.channel.in(participatingPrivateChannel))
                .transform(GroupBy.groupBy(message.channel.id)
                        .as(message.createdAt.max()));
    }

    private List<Channel> getParticipatingPrivateChannel(UUID userId) {
        return jpaQueryFactory
                .select(readStatus.channel)
                .from(readStatus)
                .where(readStatus.user.id.eq(userId))
                .fetch();
    }

    private Map<UUID, List<User>> findChannelUsersMapByChannelIds(List<UUID> channelIds) {
        return jpaQueryFactory
                .from(channel)
                .join(channel.readStatus, readStatus)
                .join(readStatus.user, user)
                .join(user.profile).fetchJoin()
                .where(channel.id.in(channelIds))
                .groupBy(channel.id)
                .transform(GroupBy.groupBy(channel.id).as(GroupBy.list(user)));
    }
}

