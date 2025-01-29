package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.file.FileBasic;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileMessageRepository implements MessageRepository {
  private Path fileDirectory;
  private FileBasic messageFileBasic;

  public FileMessageRepository(Path directory) {
    this.fileDirectory = directory;
    this.messageFileBasic = new FileBasic(fileDirectory);
  }

  @Override
  public void creat(Message message) {
    Path filePath = fileDirectory.resolve(message.getId().toString().concat(".ser"));

    try (FileOutputStream fos = new FileOutputStream(filePath.toFile());
        ObjectOutputStream oos = new ObjectOutputStream(fos);
    ) {
      oos.writeObject(message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void delete(UUID messageId) {
    try {
      Optional<Path> messageDelete = Files.list(fileDirectory)
          .filter(path -> path.getFileName().toString().equals(messageId + ".ser"))
          .findFirst();
      if(messageDelete.isPresent()) {
        Files.delete(messageDelete.get());
      }
      else {
        System.out.println("File not found for Id");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(UUID messageId, String messageContent) {
    try {
      Optional<Path> updateMessage = Files.list(fileDirectory)
          .filter(path -> path.getFileName().toString().equals(messageId + ".ser"))
          .findFirst();
      if(updateMessage.isPresent()) {
        Message message = (Message) messageFileBasic.deserialization(updateMessage.get());
        message.updateMessage(messageContent);
        creat(message);
      }
      else {
        System.out.println("File not found for Id");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Message findById(UUID messageId) {
    List<Message> messageList = messageFileBasic.load();
    Optional<Message> getMessage = messageList.stream()
        .filter(message -> message.getId().equals(messageId))
        .map(Message::new)
        .findFirst();
    if (getMessage.isEmpty()) {
      throw new IllegalArgumentException("Message not found");
    } else {
      return getMessage.get();
    }

  }

  @Override
  public List<Message> findByAll() {
    List<Message> messageList = messageFileBasic.load();
    return messageList.stream()
        .map(Message::new)
        .collect(Collectors.toList());
  }



}
