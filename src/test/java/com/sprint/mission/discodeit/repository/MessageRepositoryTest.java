package com.sprint.mission.discodeit.repository;


import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.JpaConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Import(JpaConfig.class)
@ExtendWith(SpringExtension.class) // Junit5 + Spring
@DataJpaTest
public class MessageRepositoryTest {

  @Autowired
  MessageRepository messageRepository;

  @Autowired
  ChannelRepository channelRepository;

  private Channel channel;

  private UUID channelId;

  @BeforeEach
  void setUp() {
    channel = Channel.builder()
        .type(ChannelType.PUBLIC)
        .name("Test Channel")
        .build();
    channel = channelRepository.save(channel); // Id 생성을 위해 저장
    channelId = channel.getId();

    Message message1 = Message.builder()
        .channel(channel)
        .content("Test1")
        .build();
    Message message2 = Message.builder()
        .channel(channel)
        .content("Test2")
        .build();
    Message message3 = Message.builder()
        .channel(channel)
        .content("Test3")
        .build();
    messageRepository.save(message1);
    messageRepository.save(message2);
    messageRepository.save(message3);

  }

  @Test
  @DisplayName("채널에 포함된 메세지 찾기 : 성공")
  void findByChannelId_Success() {
    /**given**/

    /**when&then**/
    List<Message> messages = messageRepository.findByChannelId(channelId);

    assertThat(messages).hasSize(3);
    assertThat(messages.get(0).getChannel().getId()).isEqualTo(channelId);
  }

  @Test
  @DisplayName("채널에 포함된 메세지 찾기 : 실패")
  void findByChannelId_Fail() {
    /**given**/
    UUID nonExistentChannelId = UUID.randomUUID();

    /**when&then**/
    List<Message> messages = messageRepository.findByChannelId(nonExistentChannelId);

    assertThat(messages).isEmpty();
  }

  @Test
  @DisplayName("페이지네이션으로 채널에 포함된 메세지 찾기 : 성공")
  void FindByChannelIdWithPageable_Success() {
    /**given**/
    Sort sort = Sort.by("createdAt").ascending();
    Pageable pageable = PageRequest.of(0, 2, sort);

    /**when&then**/
    Page<Message> messagePage = messageRepository.findByChannelId(channelId, pageable);

    assertThat(messagePage.getTotalElements()).isEqualTo(3); // 총 갯수
    assertThat(messagePage.getContent()).hasSize(2); // 2 개 불러와야 한다.
  }

  @Test
  @DisplayName("페이지네이션으로 채널에 포함된 메세지 찾기 : 성공")
  void FindByChannelIdWithPageable_Fail() {
    /**given**/
    Sort sort = Sort.by("createdAt").ascending();
    Pageable pageable = PageRequest.of(0, 2, sort);

    UUID nonExistentChannelId = UUID.randomUUID();

    /**when&then**/
    Page<Message> messagePage = messageRepository.findByChannelId(nonExistentChannelId, pageable);

    assertThat(messagePage.getTotalElements()).isEqualTo(0); // 총 갯수
    assertThat(messagePage.getContent()).isEmpty();
  }

}
