package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class Message extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  private final Channel channel;
  private final User sender;
  private String content;
  private MessageType type;
  
  private Message(Channel channel, User sender, String content, MessageType type) {
    super();
    this.channel = channel;
    this.sender = sender;
    this.content = content;
    this.type = type;
  }
  
  public static Message ofCommon(Channel channel, User sender, String content) {
    return new Message(channel, sender, content, MessageType.COMMON);
  }
  
  public static Message ofReply(Channel channel, User sender, String content) {
    return new Message(channel, sender, content, MessageType.REPLY);
  }
  
  public Channel getChannel() {
    return channel;
  }
  
  public User getSender() {
    return sender;
  }
  
  public String getContent() {
    return content;
  }
  
  public MessageType getType() {
    return type;
  }
  
  public void updateContent(String content) {
    this.content = content;
    super.update();
  }
}
