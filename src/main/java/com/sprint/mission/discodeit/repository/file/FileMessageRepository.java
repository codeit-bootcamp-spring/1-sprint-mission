package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.util.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.MESSAGE_FILE;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "file")
public class FileMessageRepository implements MessageRepository {

  @Override
  public Message create(Message message) {
    List<Message> messages = FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class);
    messages.add(message);
    FileUtil.saveAllToFile(MESSAGE_FILE, messages);
    return message;
  }

  @Override
  public Optional<Message> findById(String id) {
    List<Message> messages = FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class);
    return messages.stream().filter(m -> m.getUUID().equals(id)).findAny();
  }

  @Override
  public List<Message> findByChannel(String channelId) {
    List<Message> messages = FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class);
    return messages.stream().filter(m -> m.getChannelUUID().equals(channelId)).toList();
  }

  @Override
  public List<Message> findAll() {
    return FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class);
  }

  @Override
  public Message findLatestChannelMessage(String channelId) {
    List<Message> messages = FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class);
    return messages.stream().filter(m -> m.getChannelUUID().equals(channelId))
        .max(Comparator.comparing(Message::getCreatedAt))
        .orElse(null);
  }

  @Override
  public Message update(Message message) {
    List<Message> messages = FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class);
    Message targetMessage = messages.stream()
        .filter(m -> m.getUUID().equals(message.getUUID()))
        .findAny()
        .orElseThrow(() -> new MessageNotFoundException());
    messages.remove(targetMessage);
    messages.add(message);
    FileUtil.saveAllToFile(MESSAGE_FILE, messages);
    return message;
  }

  @Override
  public void delete(String id) {
    List<Message> messages = FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class);
    Message targetMessage = messages.stream()
        .filter(m -> m.getUUID().equals(id))
        .findAny()
        .orElseThrow(() -> new MessageNotFoundException());
    messages.remove(targetMessage);

    FileUtil.saveAllToFile(MESSAGE_FILE, messages);
  }

  @Override
  public void deleteByChannel(String channelId) {
    List<Message> messages = FileUtil.loadAllFromFile(MESSAGE_FILE, Message.class);
    messages.removeIf(message->message.getChannelUUID().equals(channelId));
    FileUtil.saveAllToFile(MESSAGE_FILE, messages);
  }

  @Override
  public void clear() {
    File file = new File(MESSAGE_FILE);
    if (file.exists()) {
      file.delete();
    }
  }
}
