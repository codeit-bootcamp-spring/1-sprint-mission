package com.sprint.mission.unit.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.unit.TestEntityFactory;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class MessageServiceUnitTest {

  @Mock
  private MessageRepository messageRepository;

  @InjectMocks
  private BasicMessageService basicMessageService;

  private Message message;
  private Message message2;

  @BeforeEach
  void setUp() {
    message = TestEntityFactory.createMessageWithNoAttachments();
    message2 = TestEntityFactory.createMessageWithNoAttachments();
  }


  @Nested
  class CreateMessage {

    @Test
    void testCreateMessage_success() {
      // given
      given(messageRepository.save(message)).willReturn(message);

      // when
      Message result = basicMessageService.createMessage(message);

      // then
      then(messageRepository).should().save(message);
    }
  }

  @Nested
  class UpdateMessage {

    @Test
    void testUpdateMessage_success() {
      String newContent = "newContent";
      given(messageRepository.save(any())).willReturn(message);

      Message result = basicMessageService.updateMessage(message, newContent);

      then(messageRepository).should().save(message);
    }
  }

  @Nested
  class FindMessage {

    @Test
    void getMessageById_success() {
      String messageId = message.getId().toString();
      given(messageRepository.findById(UUID.fromString(messageId))).willReturn(
          Optional.ofNullable(message));

      // when
      Message result = basicMessageService.getMessageById(messageId);

      // then
      assertThat(result).isEqualTo(message);
      then(messageRepository).should().findById(UUID.fromString(messageId));
    }

    @Test
    void getMessageById_fail_notFound() {
      String messageId = UUID.randomUUID().toString();
      given(messageRepository.findById(UUID.fromString(messageId))).willReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> basicMessageService.getMessageById(messageId))
          .isInstanceOf(MessageNotFoundException.class)
          .hasMessageContaining(ErrorCode.MESSAGE_NOT_FOUND.getMessage());
    }

    @Test
    void getMessagesByChannel_success() {
      UUID channelId = UUID.randomUUID();
      Pageable pageable = PageRequest.of(0, 10);
      Page<Message> page = new PageImpl<>(List.of());
      given(messageRepository.findByChannel_Id(channelId, pageable)).willReturn(page);

      // when
      Page<Message> result = basicMessageService.getMessagesByChannel(channelId.toString(),
          pageable);

      // then
      assertThat(result).isEqualTo(page);
      then(messageRepository).should().findByChannel_Id(channelId, pageable);
    }

    @Test
    void getMessagesByChannelWithCursor_success() {
      UUID channelId = UUID.randomUUID();
      Pageable pageable = PageRequest.of(0, 10);
      Instant cursor = Instant.now();
      Page<Message> page = new PageImpl<>(List.of());
      given(messageRepository.findByChannel_IdAndCreatedAtLessThan(channelId, cursor, pageable))
          .willReturn(page);
      // when
      Page<Message> result =
          basicMessageService.getMessagesByChannelWithCursor(channelId.toString(), cursor,
              pageable);
      // then
      assertThat(result).isEqualTo(page);
      then(messageRepository).should()
          .findByChannel_IdAndCreatedAtLessThan(channelId, cursor, pageable);
    }

    @Test
    void getLatestMessageByChannel_success() {
      //givne
      UUID channelId = UUID.randomUUID();
      given(messageRepository.findTopByChannel_IdOrderByCreatedAtDesc(channelId))
          .willReturn(Optional.of(message));

      // when
      Message result = basicMessageService.getLatestMessageByChannel(channelId.toString());

      // then
      assertThat(result).isEqualTo(message);
      then(messageRepository).should().findTopByChannel_IdOrderByCreatedAtDesc(channelId);
    }

    @Test
    void getLatestMessageForChannels() {
      Channel c1 = TestEntityFactory.createPrivateChannel();
      Channel c2 = TestEntityFactory.createPublicChannel();
      message2.addChannel(c2);
      message.addChannel(c1);

      given(messageRepository.findLatestMessagesForEachChannel(List.of(c1.getId(), c2.getId())))
          .willReturn(List.of(message, message2));

      //when
      Map<UUID, Instant> result = basicMessageService.getLatestMessageForChannels(List.of(c1, c2));

      //then
      assertThat(result).containsKeys(c1.getId(), c2.getId());
      then(messageRepository).should()
          .findLatestMessagesForEachChannel(List.of(c1.getId(), c2.getId()));

    }


  }

  @Nested
  class DeleteMessage {

    @Test
    void deleteMessage_success() {
      // given
      String messageId = message.getId().toString();

      // when
      basicMessageService.deleteMessage(messageId);

      //then
      then(messageRepository).should().deleteById(UUID.fromString(messageId));
    }


    @Test
    void deleteMessage_fail_whenWrongUuidFormat() {
      // given
      String messageId = "invalid id";

      //when & then
      assertThatThrownBy(() -> basicMessageService.deleteMessage(messageId))
          .isInstanceOf(IllegalArgumentException.class);

    }
  }

}

