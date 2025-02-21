package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.interfacepac.MessageRepository;

import java.util.*;
import java.util.stream.Collectors;

public class JCFMessageRepository implements MessageRepository {

  private final Map<User, List<Message>> messageData;
  ;

  public JCFMessageRepository() {
    this.messageData = new HashMap<>();
  }

  @Override
  public Message save(Message message) {
    messageData.computeIfAbsent(message.getUser(), k -> new ArrayList<>()).add(message);
    return message;
  }

  @Override
  public Optional<Message> findById(UUID id) {
    return Optional.ofNullable(messageData.values().stream()
        .flatMap(List::stream)
        .filter(message -> message.getId().equals(id))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Not found Message")));
  }

  @Override
  public List<Message> findAll() {
    List<Message> allMessages = new ArrayList<>();
    messageData.values().forEach(allMessages::addAll);
    return allMessages;
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageData.values().stream()
        .flatMap(List::stream)
        .filter(message -> message.getChannel().getId().equals(channelId))
        .collect(Collectors.toList());
  }


  @Override
  public void deleteByChannel(Channel channel) {
    messageData.values()
        .forEach(
            allMessages -> allMessages.removeIf(message -> message.getChannel().equals(channel)));
  }

  @Override
  public void deleteById(UUID messageId) {
    messageData.values()
        .forEach(messages
            -> messages.removeIf(message -> message.getId().equals(messageId)));
  }

}
