package com.sprint.mission.repository;

import static com.sprint.mission.entity.QChannel.channel;
import static com.sprint.mission.entity.QMessage.message;
import static com.sprint.mission.entity.QReadStatus.readStatus;
import static com.sprint.mission.entity.QUser.user;

import com.querydsl.core.group.GroupBy;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.dto.ChannelMapper;
import com.sprint.mission.dto.response.ChannelDto;
import com.sprint.mission.entity.BaseEntity;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import jakarta.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.mapstruct.factory.Mappers;

import java.time.Instant;


public class CustomChannelRepositoryImpl implements CustomChannelRepository {

  private final JPAQueryFactory jpaQueryFactory;
  private final ChannelMapper channelMapper = Mappers.getMapper(ChannelMapper.class);

  public CustomChannelRepositoryImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  public List<ChannelDto> findAllPrivateChannelByUserId(UUID userId) {

    // 쿼리1
    List<Channel> participatingPrivateChannel = getParticipatingPrivateChannel(userId);
    if (participatingPrivateChannel.isEmpty()) {
      return Collections.emptyList();
    }
    List<UUID> channelIds = participatingPrivateChannel.stream().map(BaseEntity::getId).toList();

    // 쿼리2
    Map<UUID, List<User>> usersInChannelMap = findChannelUsersMapByChannelIds(channelIds);
    // 쿼리3
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

