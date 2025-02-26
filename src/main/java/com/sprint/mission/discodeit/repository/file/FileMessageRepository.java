package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import java.util.HashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileMessageRepository implements MessageRepository {

  private HashMap<UUID, Message> data;
  private SerializationUtil<UUID, Message> util;

  public FileMessageRepository(SerializationUtil<UUID, Message> util) {
    this.util = util;
    this.data = util.loadData();
  }

  @Override
  public Message save(Message message) {
    this.data.put(message.getId(), message);
    util.saveData(this.data);
    return message;
  }

  @Override
  public Optional<Message> findById(UUID messageId) {
    return Optional.ofNullable(this.data.get(messageId));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return this.data.values().stream().filter(message -> message.getChannelId().equals(channelId))
        .toList();
  }

  @Override
  public boolean existsById(UUID messageId) {
    return this.data.containsKey(messageId);
  }

  @Override
  public void deleteById(UUID messageId) {
    this.data.remove(messageId);
    util.saveData(this.data);
  }

  @Override
  public void deleteAllByChannelId(UUID channelId) {
    this.findAllByChannelId(channelId)
        .forEach(message -> this.deleteById(message.getId()));
    util.saveData(this.data);
  }
}
