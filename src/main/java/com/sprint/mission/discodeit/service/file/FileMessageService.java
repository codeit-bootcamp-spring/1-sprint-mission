/*
package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.MessageValidationException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.FileConstant.MESSAGE_FILE;

public class FileMessageService implements MessageService {
  private static volatile FileMessageService messageRepository;
  private UserService userService;
  private ChannelService channelService;

  private FileMessageService(UserService userService, ChannelService channelService) {
    this.userService = userService;
    this.channelService = channelService;
  }

  public static FileMessageService getInstance(UserService userService, ChannelService channelService) {
    if (messageRepository == null) {
      synchronized (FileMessageService.class) {
        if (messageRepository == null) {
          messageRepository = new FileMessageService(userService, channelService);
          initFile();
        }
      }
    }
    return messageRepository;
  }

  private static void initFile() {
    File file = new File(MESSAGE_FILE);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        throw new RuntimeException("파일 생성중 오류." + e.getMessage());
      }
    }
  }

  @Override
  public Message createMessage(String userId, Message message, ChatChannel channel) throws MessageValidationException {
    if(!checkUserExists(userId)) throw new MessageValidationException();
    if(!checkChannelExists(channel)) throw new MessageValidationException();
    message.setChannelid(channel.getId());
    saveMessageToFile(message);
    return message;
  }

  private boolean checkChannelExists(Channel channel){
    return channelService.getChannelById(channel.getId()).;
  }

  @Override
  public Optional<Message> getMessageById(String messageId, ChatChannel channel) {
    System.out.println(loadAllMessages());
    return loadAllMessages().stream().filter(m -> m.getId().equals(messageId)).findFirst();
  }

  @Override
  public List<Message> getMessagesByChannel(ChatChannel channel) {
    return loadAllMessages().stream().filter(message -> message.getChannelid().equals(channel.getId())).toList();
  }

  @Override
  public Message updateMessage(ChatChannel channel, String messageId, MessageUpdateDto updatedMessage) {
    List<Message> messages = loadAllMessages();
    Message message = messages.stream().filter(m -> m.getId().equals(messageId)).findFirst().get();

    synchronized (message) {
      updatedMessage.getContentUrl().ifPresent(message::setContentImage);
      updatedMessage.getContent().ifPresent(message::setContent);
    }
    saveMessageToFile(messages);

    return message;
  }

  @Override
  public boolean deleteMessage(String messageId, ChatChannel channel) {
    List<Message> messages = loadAllMessages();
    Message message = messages.stream().filter(m -> m.getId().equals(messageId)).findFirst().get();
    messages.remove(message);
    return true;
  }

  private boolean checkUserExists(String userId) {
    return userService.findUserById(userId).isPresent();
  }
  private void saveMessageToFile(Message message){
    List<Message> messages = loadAllMessages();
    messages.add(message);
    saveMessageToFile(messages);
  }

  private List<Message> loadAllMessages(){
    List<Message> messages = new ArrayList<>();
    try (FileInputStream fos = new FileInputStream(MESSAGE_FILE);
         ObjectInputStream ois = new ObjectInputStream(fos)) {
      while (true) {
        try {
          Message message = (Message) ois.readObject();
          messages.add(message);
        } catch (EOFException e) {
          break;
        } catch (ClassNotFoundException e) {
          throw new RuntimeException("직렬화된 객체 클래스가 존재하지 않습니다.", e);
        }
      }
    } catch (IOException e) {
      return messages;
    }
    return messages;
  }
  private void saveMessageToFile(List<Message> messages) {
    try (
        FileOutputStream fos = new FileOutputStream(MESSAGE_FILE);
        ObjectOutputStream oos = new ObjectOutputStream(fos)
    ) {
      for (Message m : messages) {
        oos.writeObject(m);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
*/
