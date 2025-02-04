package com.sprint.mission.discodeit.exception.channel;

public class ChannelNotFoundException extends RuntimeException {
    public ChannelNotFoundException() {
        super("채널이 존재하지 않습니다.");
    }

    public ChannelNotFoundException(String message) {
        super(message);
    }
}
