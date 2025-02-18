package com.sprint.mission.discodeit.exepction;

public class ChannelNotFoundException extends RuntimeException {
    public ChannelNotFoundException(String channelId) {
        super("채널을 찾을 수 없습니다: " + channelId);
    }
}