package com.sprint.mission.discodeit.repository;

import static com.sprint.mission.discodeit.entity.binarycontent.QBinaryContent.binaryContent;
import static com.sprint.mission.discodeit.entity.channel.QChannel.channel;
import static com.sprint.mission.discodeit.entity.message.QMessage.message;
import static com.sprint.mission.discodeit.entity.user.QUser.user;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.discodeit.entity.binarycontent.QBinaryContent;
import com.sprint.mission.discodeit.entity.message.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageQuerydslRepository {

  private final JPAQueryFactory queryFactory;

  private final QBinaryContent attachmentContent = new QBinaryContent("attachmentContent");

  @Override
  public Slice<Message> cursorBasedPaging(UUID channelId, Instant cursor, Pageable slice) {

    List<Message> messages = queryFactory
        .select(message)
        .from(message)
        .join(message.channel, channel).fetchJoin()
        .join(message.author, user).fetchJoin()
        .leftJoin(user.profile, binaryContent).fetchJoin()
        .leftJoin(message.attachments, attachmentContent)
        .orderBy(
            orderByField(slice)
        )
        .where(
            channelIdEq(channelId),
            cursor(cursor, slice)
        )
        .limit(slice.getPageSize() + 1)
        .fetch();

    boolean hasNext = messages.size() > getSize(slice);

    return new SliceImpl<>(messages, slice, hasNext);
  }

  @Override
  public Long totalCount(UUID channelId, Instant cursor, Pageable slice) {
    return queryFactory
        .select(message.count())
        .from(message)
        .join(message.channel, channel)
        .join(message.author, user)
        .leftJoin(user.profile, binaryContent)
        .leftJoin(message.attachments, attachmentContent)
        .where(
            message.channel.id.eq(channelId)
        )
        .fetchOne();
  }

  private BooleanExpression channelIdEq(UUID channelId) {
    System.out.println("channelId = " + channelId);
    return channelId != null ? message.channel.id.eq(channelId) : null;
  }

  private int getSize(Pageable slice) {
    return slice.getPageSize() == 0 ? 10 : slice.getPageSize();
  }

  private BooleanExpression cursor(Instant cursor, Pageable slice) {
    if (cursor == null) {
      return null;
    }

    boolean isDesc = true;

    Sort sort = slice.getSort();
    if (sort.isSorted()) {
      for (Order order : sort) {
        if ("createdAt".equals(order.getProperty())) {
          isDesc = order.getDirection().isDescending();
          break;
        }
      }
    }

    return isDesc
        ? message.createdAt.loe(cursor)
        : message.createdAt.goe(cursor);
  }


  private OrderSpecifier<?> orderByField(Pageable slice) {
    Sort sort = slice.getSort();

    if (sort.isEmpty()) {
      return message.createdAt.desc();
    }

    for (Order order : sort) {
      String property = order.getProperty();
      Direction direction = order.getDirection();

      if (property.equals("createdAt")) {
        if (direction.isDescending()) {
          return message.createdAt.desc();
        }
      }
    }

    return message.createdAt.asc();
  }

}
