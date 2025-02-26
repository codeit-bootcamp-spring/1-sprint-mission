package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.entity.user.UserRole;

import java.util.Map;
import java.util.UUID;

public class BaseEntityFactory implements EntityFactory {

  private BaseEntityFactory() {
  }

  private static class SingleInstanceHolder {

    private static final EntityFactory INSTANCE = new BaseEntityFactory();
  }

  public static EntityFactory getInstance() {
    return SingleInstanceHolder.INSTANCE;
  }

  @Override
  public User createUser(String userName, String userEmail, String userPassword) {
    return null;
  }

  @Override
  public User createUser(String userName, String userEmail, String userPassword,
      UserRole userRole) {
    return new User(userName, userEmail, userPassword, userRole);
  }

  @Override
  public Channel createChannel(UUID channelId) {
    return new Channel(channelId);
  }

//    @Override
//    public User createUser(String userName, String userEmail, String userPassword) {
//        return new User(userName, userEmail, userPassword);
//    }

  @Override
  public Channel createChannel(String channelName, User owner, String channelType,
      Map<UUID, User> userList) {
    return new Channel(channelName, owner, channelType, userList);
  }

  @Override
  public Message createMessage(String title, String content, User sender, User receiver) {
    return new Message(title, content, sender, receiver);
  }

  @Override
  public Message createMessage(UUID messageId, String title, String content, User sender,
      User receiver) {
    return new Message(messageId, title, content, sender, receiver);
  }
}
