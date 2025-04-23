package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.Type;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@EnableJpaAuditing
@ActiveProfiles("test")
class MessageRepositoryTest {

  @Autowired
  MessageRepository messageRepository;

  @Autowired
  UserRepository userRepository;
  @Autowired
  ChannelRepository channelRepository;

  @Autowired
  TestEntityManager em;

  BinaryContent content;
  User user;
  Channel channel;
  Message message;

  @BeforeEach
  void setUp() {
    content = BinaryContent.create(1024L, "attachment", "jpg");
    user = User.create("username", "email", "password");
    UserStatus userStatus = UserStatus.create(user);
    user.updateProfile(content);
    channel = Channel.create(Type.PUBLIC, "public", "public channel");

    userRepository.save(user);
    channelRepository.save(channel);

    message = Message.create(user, "hello", channel, List.of(content));
    messageRepository.save(message);

    em.flush();
    em.clear();
  }

  @Test
  @DisplayName("findByIdWithAttachments")
  void findByIdWithAttachments() {

    Message findMessage = messageRepository
        .findByIdWithAttachments(message.getId()).get();

    BinaryContent attachment = findMessage.getAttachments().get(0);

    assertThat(findMessage.getId()).isEqualTo(message.getId());
    assertThat(attachment.getId()).isEqualTo(content.getId());
  }

  @Test
  @DisplayName("findByChannelIdWithAttachments")
  void findByChannelIdWithAttachments() {
    List<Message> findMessages = messageRepository
        .findByChannelIdWithAttachments(channel.getId());

    Message findMessage = findMessages.get(0);
    BinaryContent attachment = findMessages.get(0).getAttachments().get(0);
    assertThat(findMessage.getId()).isEqualTo(message.getId());
    assertThat(findMessage.getChannel().getId()).isEqualTo(channel.getId());
    assertThat(attachment.getId()).isEqualTo(content.getId());
  }

  @Test
  @DisplayName("findPageByChannelId")
  void findPageByChannelId() {
    Slice<Message> slice = messageRepository
        .findPageByChannelId(channel.getId(), PageRequest.of(0, 10));

    Message findMessage = slice.getContent().get(0);
    assertThat(slice.hasNext()).isFalse();
    assertThat(findMessage.getId()).isEqualTo(message.getId());
    assertThat(findMessage.getChannel().getId()).isEqualTo(channel.getId());
    assertThat(findMessage.getAttachments().get(0).getId()).isEqualTo(content.getId());
  }

  @Test
  @DisplayName("findPageByChannelIdWithCursor1")
  void findPageByChannelIdWithCursor1() {
    Slice<Message> slice = messageRepository
        .findPageByChannelIdWithCursor(channel.getId(), message.getCreatedAt().minusSeconds(10),
            PageRequest.of(0, 10));

    assertThat(slice.hasNext()).isFalse();
    assertThat(slice.getContent()).isEmpty();
  }

  @Test
  @DisplayName("findPageByChannelIdWithCursor2")
  void findPageByChannelIdWithCursor2() {
    Slice<Message> slice = messageRepository
        .findPageByChannelIdWithCursor(channel.getId(), message.getCreatedAt().plusSeconds(10),
            PageRequest.of(0, 10));

    assertThat(slice.hasNext()).isFalse();
    assertThat(slice.getContent()).isNotEmpty();
    assertThat(slice.getContent().get(0).getId()).isEqualTo(message.getId());
  }
}