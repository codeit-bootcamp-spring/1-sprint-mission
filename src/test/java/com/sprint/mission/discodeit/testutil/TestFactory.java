package com.sprint.mission.discodeit.testutil;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.lang.reflect.Constructor;
import java.time.Instant;
import java.util.UUID;
import org.springframework.test.util.ReflectionTestUtils;

public class TestFactory {

  public static Channel createChannel(Channel.Type type, String name, String description) {
    try {
      Constructor<Channel> constructor = Channel.class.getDeclaredConstructor();
      constructor.setAccessible(true);
      Channel channel = constructor.newInstance();
      ReflectionTestUtils.setField(channel, "type", type);
      ReflectionTestUtils.setField(channel, "name", name);
      ReflectionTestUtils.setField(channel, "description", description);
      ReflectionTestUtils.setField(channel, "id", UUID.randomUUID()); // 필요한 경우
      ReflectionTestUtils.setField(channel, "createdAt", Instant.now());
      return channel;
    } catch (Exception e) {
      throw new RuntimeException("Channel 인스턴스 생성 실패", e);
    }
  }

  public static User createUser(UUID id, String name) {
    try {
      Constructor<User> constructor = User.class.getDeclaredConstructor();
      constructor.setAccessible(true);
      User user = constructor.newInstance();
      ReflectionTestUtils.setField(user, "id", id);
      ReflectionTestUtils.setField(user, "name", name);
      return user;
    } catch (Exception e) {
      throw new RuntimeException("User 인스턴스 생성 실패", e);
    }
  }

  public static ReadStatus createReadStatus(User user, Channel channel, Instant createdAt) {
    try {
      Constructor<ReadStatus> constructor = ReadStatus.class.getDeclaredConstructor();
      constructor.setAccessible(true);
      ReadStatus status = constructor.newInstance();
      ReflectionTestUtils.setField(status, "user", user);
      ReflectionTestUtils.setField(status, "channel", channel);
      ReflectionTestUtils.setField(status, "createdAt", createdAt);
      return status;
    } catch (Exception e) {
      throw new RuntimeException("ReadStatus 인스턴스 생성 실패", e);
    }

  }

}
