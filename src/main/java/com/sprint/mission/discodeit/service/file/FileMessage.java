
package com.sprint.mission.discodeit.service.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.file.repository.FileMessageRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileMessage implements FileMessageRepository {
  private static final String FILE_PATH = "src/main/java/com/sprint/mission/discodeit/service/file/data/message/message.json";
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private List<Message> readAllMessages() {
    File file = new File(FILE_PATH);
    if (!file.exists() || file.length() == 0)
      return new ArrayList<>();
    try {
      return objectMapper.readValue(file, new TypeReference<List<Message>>() {
      });
    } catch (IOException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  @Override
  public void creat(UUID userId, String content, UUID channelId, FileUser fileUser) {
    Message message = new Message(userId, content, channelId);
    try {
      List<Message> messageList = readAllMessages();
      messageList.add(message);
      saveToFile(messageList);
      fileUser.addMessage(message.getId(), userId);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void delete(UUID messageId) {
    try {
      List<Message> messageList = readAllMessages();
      messageList.removeIf(message -> message.getId().equals(messageId));
      saveToFile(messageList);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(UUID messageId, String content) {
    List<Message> messageList = readAllMessages();

    boolean updated = messageList.stream()
        .filter(message -> message.getId().equals(messageId))
        .findFirst()
        .map(message -> {
          message.updateMessage(content);
          return true;
        })
        .orElse(false);

    if (updated) {
      saveToFile(messageList);
    } else {
      throw new IllegalArgumentException("Message with ID not found.");
    }
  }

  private void saveToFile(List<Message> messageList) {
    try {
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), messageList);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  @Override
  public List<String> getMessageList(List<UUID> messageIdList, FileUser fileUser) {
    List<Message> messageList = readAllMessages();
    if (messageIdList.isEmpty()) {
      System.out.println("There is no chat in the channel");
      return null;
    }
    else {
      return messageList.stream()
          .filter(message -> messageIdList.stream()
              .anyMatch(messageId -> message.getId().equals(messageId)))
          .map(message -> "name" +fileUser.getName(message.getUserId()) + "  " + message.getContent())
          .collect(Collectors.toList());
    }
  }

  @Override
  public List<Message> findByAll() {
    List<Message> messageList = readAllMessages();
    return messageList.stream()
        .map(Message::new)
        .collect(Collectors.toList());
  }

}