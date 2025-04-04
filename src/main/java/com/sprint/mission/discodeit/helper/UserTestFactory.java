package com.sprint.mission.discodeit.helper;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;

public class UserTestFactory {

  public static User create(String username, String email, String password) {
    return User.builder()
        .username(username)
        .email(email)
        .password(password)
        .build();
  }

  public static User createWithProfile(String username, String email, String password,
      BinaryContent profile) {
    return User.builder()
        .username(username)
        .email(email)
        .password(password)
        .profile(profile)
        .build();
  }

  public static User createRandom() {
    return User.builder()
        .username("user_" + UUID.randomUUID())
        .email(UUID.randomUUID() + "@test.com")
        .password("pw")
        .build();
  }
}
