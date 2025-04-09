package com.sprint.mission.unit;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import org.springframework.test.util.ReflectionTestUtils;

public class TestEntityFactory {

  public static User createUser(String name, String email) {
    User user = new User(name, email, "password", null, null);
    ReflectionTestUtils.setField(user, "id", UUID.randomUUID());
    return user;
  }

  public static Channel createPublicChannel() {
    Channel channel = new Channel(ChannelType.PUBLIC, "public", "public channel", null);
    ReflectionTestUtils.setField(channel, "id", UUID.randomUUID());

    return channel;
  }

  public static Channel createPrivateChannel() {
    Channel channel = new Channel(ChannelType.PRIVATE, "private", null, null);
    ReflectionTestUtils.setField(channel, "id", UUID.randomUUID());
    return channel;
  }

  public static Message createMessageWithNoAttachments() {
    Message message = new Message("content", null, null, new ArrayList<>());
    ReflectionTestUtils.setField(message, "id", UUID.randomUUID());
    ReflectionTestUtils.setField(message, "createdAt", Instant.now());
    return message;
  }


  public static ReadStatus createReadStatus(Channel channel, User user) {
    ReadStatus status = new ReadStatus(channel, user);
    ReflectionTestUtils.setField(status, "id", UUID.randomUUID());
    ReflectionTestUtils.setField(status, "lastReadAt", Instant.now().minusSeconds(10));
    return status;
  }

  public static BinaryContent createBinaryContent() {
    BinaryContent content = new BinaryContent("testFile", 100L, "image/jpeg");
    ReflectionTestUtils.setField(content, "id", UUID.randomUUID());
    return content;
  }
}
