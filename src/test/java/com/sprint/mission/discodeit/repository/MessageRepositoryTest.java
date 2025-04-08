package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.config.QuerydslConfig;
import com.sprint.mission.discodeit.entity.message.Message;
import com.sprint.mission.discodeit.logging.LogConfiguration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataJpaTest
@ActiveProfiles("test")
@Import({LogConfiguration.class, QuerydslConfig.class})
@ExtendWith(SpringExtension.class)
class MessageRepositoryTest {

  @Autowired
  MessageRepository messageRepository;

  @Autowired
  ChannelRepository channelRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  TestEntityManager em;

  @Test
  @DisplayName("메세지 5개 조회")
  void searchMessage() throws Exception {
    UUID channelId = UUID.fromString("6f17a8d1-77d7-437d-811e-d98db3bd30bc");

    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Direction.DESC, "createdAt"));

    Slice<Message> messages = messageRepository.cursorBasedPaging(channelId, Instant.now(),
        pageRequest);
    List<Message> content = messages.getContent();

    assertThat(content).isNotEmpty();
    assertThat(content.size()).isEqualTo(6); // 커서 페이징을 위해 repository 단에서는 1개 더 쿼리함
  }

  @Test
  @DisplayName("메세지 커서 페이징")
  void cursorBasedPagination() throws Exception {
    UUID channelId = UUID.fromString("6f17a8d1-77d7-437d-811e-d98db3bd30bc");

    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Direction.DESC, "createdAt"));

    Slice<Message> messages1 = messageRepository.cursorBasedPaging(channelId,
        Instant.now(),
        pageRequest);
    List<Message> content1 = messages1.getContent();

    assertThat(content1).isNotEmpty();
    assertThat(content1.size()).isEqualTo(6); // 커서 페이징을 위해 repository 단에서는 1개 더 쿼리함

    Instant cursor = content1.get(content1.size() - 1).getCreatedAt();
    Slice<Message> messages2 = messageRepository.cursorBasedPaging(channelId, cursor, pageRequest);
    List<Message> content2 = messages2.getContent();

    assertThat(content2).isNotEmpty();
    assertThat(cursor).isEqualTo(content2.get(0).getCreatedAt());
  }
}
