package com.sprint.mission.discodeit.repository.jpa.message;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepository {

  private final MessageJpaRepository messageRepository;

  @Override
  public Message saveMessage(Message message) {
    return messageRepository.save(message);
  }

  @Override
  public Optional<Message> findMessageById(UUID messageId) {
    return messageRepository.findById(messageId);
  }

  @Override
  public Page<Message> findPagedMessagesByChannel(Channel channel, Pageable pageable) {
    return messageRepository.findAllByChannel(channel, pageable);
  }

  @Override
  public Slice<Message> findSlicedMessagesByChannel(Channel channel, Pageable pageable) {
    return messageRepository.findAllByChannel(channel, pageable);
  }

  @Override
  public Slice<Message> findSlicedMessagesByChannel(Channel channel, Instant cursor,
      Pageable pageable) {
    return messageRepository.findAllByChannelAndCreatedAtAfter(channel, cursor, pageable);
  }

  @Override
  public Optional<Message> findLastMessage() {
    return messageRepository.findFirstByOrderByCreatedAtDesc();
  }

  @Override
  public void removeMessage(UUID messageId) {
    messageRepository.deleteById(messageId);
  }
}
