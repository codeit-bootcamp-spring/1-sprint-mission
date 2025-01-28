package com.sprint.mission.discodeit.entity;

public class ChatChannel extends BaseChannel {

  private final boolean isChat = true;
  private ChatChannel(ChatChannelBuilder builder) {
    super(builder);
  }


  public static class ChatChannelBuilder extends BaseChannelBuilder<ChatChannelBuilder> {
    public ChatChannelBuilder(String channelName) {
      super(channelName);
    }

    @Override
    protected ChatChannelBuilder self() {
      return this;
    }

    @Override
    public ChatChannel build() {
      return new ChatChannel(this);
    }
  }


  @Override
  public String toString() {
    return "채팅채널입니다: " + super.toString();
  }

}
