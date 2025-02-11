package com.sprint.mission.discodeit.exepction;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("유저를 찾을 수 없습니다: " + userId);
    }
}