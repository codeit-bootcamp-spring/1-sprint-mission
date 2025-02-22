package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileMessageRepository implements MessageRepository {
  private final Map<UUID, Message> data;
  public FileMessageRepository(SerializationUtil<UUID, Message> util) {
    this.data = util.loadData(); // 이 부분, filePath 매개변수 이해 잘 안됨
  }

  @Override
  public Message save(Message message){
    this.data.put(message.getId(), message);
    return message;
  }

  @Override
  public Optional<Message> findById(UUID messageId){
    return Optional.ofNullable(this.data.get(messageId));
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId){
    return this.data.values().stream().filter(message -> message.getChannelId().equals(channelId)).toList();
  }
  @Override
  public boolean existsById(UUID messageId) {
    return this.data.containsKey(messageId);
  }

  @Override
  public void deleteById(UUID messageId) {
    this.data.remove(messageId);
  }

  @Override
  public void deleteAllByChannelId(UUID channelId) {
    this.findAllByChannelId(channelId)
            .forEach(message -> this.deleteById(message.getId()));
  }
}
