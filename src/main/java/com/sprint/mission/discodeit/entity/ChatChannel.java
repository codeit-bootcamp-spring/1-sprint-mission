package com.sprint.mission.discodeit.entity;

public class ChatChannel extends BaseChannel {

  private ChatChannel(ChatChannelBuilder builder) {
    super(builder);
  }

  public static class ChatChannelBuilder extends BaseChannelBuilder<ChatChannelBuilder> {
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
