package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.AbstractFileRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.MESSAGE_FILE;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "file")
public class FileMessageRepository extends AbstractFileRepository<Message> implements MessageRepository{

  public FileMessageRepository(@Value("${app.file.message-file}") String filePath) {
    super(filePath);
  }

  @Override
  public Message create(Message message) {
    List<Message> messages = loadAll(Message.class);
    messages.add(message);
    saveAll(messages);
    return message;
  }

  @Override
  public Optional<Message> findById(String id) {
    List<Message> messages = loadAll(Message.class);
    return messages.stream().filter(m -> m.getUUID().equals(id)).findAny();
  }

  @Override
  public List<Message> findByChannel(String channelId) {
    List<Message> messages = loadAll(Message.class);
    return messages.stream().filter(m -> m.getChannelUUID().equals(channelId)).toList();
  }

  @Override
  public List<Message> findAll() {
    return loadAll(Message.class);
  }

  @Override
  public Message findLatestChannelMessage(String channelId) {
    List<Message> messages = loadAll(Message.class);
    return messages.stream().filter(m -> m.getChannelUUID().equals(channelId))
        .max(Comparator.comparing(Message::getCreatedAt))
        .orElse(null);
  }

  @Override
  public Message update(Message message) {
    List<Message> messages = loadAll(Message.class);
    Message targetMessage = messages.stream()
        .filter(m -> m.getUUID().equals(message.getUUID()))
        .findAny()
        .orElseThrow(() -> new MessageNotFoundException());
    messages.remove(targetMessage);
    messages.add(message);
    saveAll(messages);
    return message;
  }

  @Override
  public void delete(String id) {
    List<Message> messages = loadAll(Message.class);
    Message targetMessage = messages.stream()
        .filter(m -> m.getUUID().equals(id))
        .findAny()
        .orElseThrow(() -> new MessageNotFoundException());
    messages.remove(targetMessage);

    saveAll(messages);
  }

  @Override
  public void deleteByChannel(String channelId) {
    List<Message> messages = loadAll(Message.class);

    messages.removeIf(message -> message.getChannelUUID().equals(channelId));

    saveAll(messages);
  }

  @Override
  public void clear() {
    File file = new File(getFilePath());
    if (file.exists()) {
      file.delete();
    }
  }
}
