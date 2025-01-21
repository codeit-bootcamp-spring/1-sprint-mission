package com.sprint.mission.discodeit.entity;

public class VoiceChannel extends BaseChannel {
  VoiceChannel(VoiceChannelBuilder builder) {
    super(builder);
  }

  public static class VoiceChannelBuilder extends BaseChannelBuilder<VoiceChannelBuilder> {

    public VoiceChannelBuilder(String channelName) {
      super(channelName);
    }

    @Override
    protected VoiceChannelBuilder self() {
      return this;
    }

    @Override
    public VoiceChannel build() {
      return new VoiceChannel(this);
    }
  }

  @Override
  public String toString() {
    return "음성채널입니다: " + super.toString();
  }
}
