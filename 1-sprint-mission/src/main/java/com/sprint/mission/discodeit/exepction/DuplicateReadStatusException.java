package com.sprint.mission.discodeit.exepction;

public class DuplicateReadStatusException extends RuntimeException {
    public DuplicateReadStatusException(String channelId, String userId) {
        super("이미 해당 채널(" + channelId + ")에 대한 ReadStatus가 존재합니다. 유저: " + userId);
    }
}