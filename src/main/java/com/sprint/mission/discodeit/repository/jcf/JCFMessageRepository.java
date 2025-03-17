package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

//@Repository
@NoArgsConstructor
//@JCFRepositoryCondition
public class JCFMessageRepository implements MessageRepository {

  private final Map<UUID, Message> messageData = new HashMap<>();

  @Override
  public Message saveMessage(Message message) {
    messageData.put(message.getId(), message);
    return messageData.get(message.getId());
  }

  @Override
  public Optional<Message> findMessageById(UUID messageId) {
    return Optional.ofNullable(messageData.get(messageId));
  }

  @Override
  public Slice<Message> findSlicedMessagesByChannel(Channel channel, Pageable pageable) {
    return null;
  }

  @Override
  public Slice<Message> findSlicedMessagesByChannel(Channel channel, Instant cursor,
      Pageable pageable) {
    return null;
  }

  @Override
  public Page<Message> findPagedMessagesByChannel(Channel channel, Pageable pageable) {
//    return messageData.values().stream()
//        .filter(message -> message.getChannel() == channel)
//        .toList();
    return null;
  }

  @Override
  public Optional<Message> findLastMessage() {
    return messageData.values().stream()
        .max(Comparator.comparing(message -> message.getCreatedAt()));
  }

  @Override
  public void removeMessage(UUID messageId) {
    messageData.remove(messageId);
  }
}
