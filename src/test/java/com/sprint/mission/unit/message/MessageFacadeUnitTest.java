package com.sprint.mission.unit.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.facade.message.MessageFacadeImpl;
import com.sprint.mission.discodeit.service.message.MessageManagementService;
import com.sprint.mission.unit.TestEntityFactory;
import java.time.Instant;
import java.util.List;
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
public class MessageFacadeUnitTest {

  @Mock
  private MessageManagementService messageManagementService;
  @Mock
  private MessageMapper messageMapper;

  @InjectMocks
  private MessageFacadeImpl messageFacade;


  private Message message;
  private User author;
  private Channel channel;

  @BeforeEach
  void setUp() {
    author = TestEntityFactory.createUser("u", "u@gmail.com");
    channel = TestEntityFactory.createPrivateChannel();
    message = TestEntityFactory.createMessageWithNoAttachments();
  }

  @Nested
  class CreateMessage {

    @Test
    void createMessage_shouldCall_success() {
      // given
      CreateMessageDto request = new CreateMessageDto("content", channel.getId().toString(),
          author.getId().toString());
      MessageResponseDto response = mock(MessageResponseDto.class);

      given(messageManagementService.createMessage(request.content(), request.channelId(),
          request.authorId(), null))
          .willReturn(message);
      given(messageMapper.toResponseDto(message))
          .willReturn(response);

      // when
      MessageResponseDto result = messageFacade.createMessage(request, null);

      // then

      assertThat(result).isEqualTo(response);
      then(messageManagementService).should()
          .createMessage(request.content(), request.channelId(), request.authorId(), null);
      then(messageMapper).should().toResponseDto(message);
    }

    @Test
    void createMessage_shouldFail_whenWrongId() {
      // given
      CreateMessageDto request = new CreateMessageDto("content", channel.getId().toString(),
          author.getId().toString());

      given(messageManagementService.createMessage(request.content(), request.channelId(),
          request.authorId(), null))
          .willThrow(new IllegalArgumentException());

      // when & then
      assertThatThrownBy(() -> messageFacade.createMessage(request, null)).isInstanceOf(
          IllegalArgumentException.class);
    }
  }


  @Nested
  class FindMessage {

    @Test
    void findMessageById_shouldCall_success() {
      // given
      String id = message.getId().toString();
      MessageResponseDto response = mock(MessageResponseDto.class);
      given(messageManagementService.findSingleMessage(id))
          .willReturn(message);
      given(messageMapper.toResponseDto(message))
          .willReturn(response);
      // when
      MessageResponseDto result = messageFacade.findMessageById(id);

      //then
      assertThat(result).isEqualTo(response);
      then(messageManagementService).should().findSingleMessage(id);
      then(messageMapper).should().toResponseDto(message);
    }

    @Test
    void findMessageById_shouldFail_wrongId() {
      //given
      String id = UUID.randomUUID().toString();
      given(messageManagementService.findSingleMessage(id))
          .willThrow(new IllegalArgumentException());

      //when & then
      assertThatThrownBy(() -> messageFacade.findMessageById(id))
          .isInstanceOf(IllegalArgumentException.class
          );
    }

    @Test
    void findMessagesByChannel_shouldCall_success() {
      // given
      String channelId = channel.getId().toString();
      Instant cursor = Instant.now();
      Pageable pageable = PageRequest.of(0, 10);
      Page<Message> page = new PageImpl<>(List.of(message));

      MessageResponseDto responseDto = mock(MessageResponseDto.class);
      Instant expectedCreatedAt = Instant.now();
      given(responseDto.createdAt()).willReturn(expectedCreatedAt);

      given(messageManagementService.findMessagesByChannel(channelId, cursor, pageable))
          .willReturn(page);
      given(messageMapper.fromEntityList(page.getContent()))
          .willReturn(List.of(responseDto));

      // when
      PageResponse<MessageResponseDto> result = messageFacade.findMessagesByChannel(channelId,
          cursor,
          pageable);

      // then
      assertThat(result.content()).isEqualTo(List.of(responseDto));
      assertThat(result.nextCursor()).isEqualTo(expectedCreatedAt);
      assertThat(result.size()).isEqualTo(pageable.getPageSize());

      then(messageManagementService).should().findMessagesByChannel(channelId, cursor, pageable);
      then(messageMapper).should().fromEntityList(page.getContent());
    }

    @Test
    void findMessagesByChannel_shouldFail_whenChannelIdEmpty() {
      // given
      String channelId = "";
      Instant cursor = Instant.now();
      Pageable pageable = PageRequest.of(0, 10);
      // when & then
      assertThatThrownBy(() -> messageFacade.findMessagesByChannel(channelId, cursor, pageable))
          .isInstanceOf(DiscodeitException.class);
      then(messageManagementService).shouldHaveNoInteractions();
      then(messageMapper).shouldHaveNoInteractions();
    }

    @Test
    void findMessagesByChannel_shouldFail_whenExceptionPropagated() {
      String channelId = channel.getId().toString();
      Instant cursor = Instant.now();
      Pageable pageable = PageRequest.of(0, 10);
      given(messageManagementService.findMessagesByChannel(channelId, cursor, pageable))
          .willThrow(new IllegalArgumentException());

      //when & then
      assertThatThrownBy(() -> messageFacade.findMessagesByChannel(channelId, cursor, pageable))
          .isInstanceOf(IllegalArgumentException.class);
      then(messageMapper).shouldHaveNoInteractions();
    }

  }


  @Nested
  class UpdateMessage {

    @Test
    void updateMessage_shouldCall_success() {
      // given
      String messageId = message.getId().toString();
      MessageUpdateDto dto = new MessageUpdateDto("new Content");
      MessageResponseDto responseDto = mock(MessageResponseDto.class);
      given(messageManagementService.updateMessage(messageId, dto.newContent()))
          .willReturn(message);
      given(messageMapper.toResponseDto(message))
          .willReturn(responseDto);

      //when
      MessageResponseDto result = messageFacade.updateMessage(messageId, dto);

      // then
      assertThat(result).isEqualTo(responseDto);
      then(messageManagementService).should().updateMessage(messageId, dto.newContent());
      then(messageMapper).should().toResponseDto(message);
    }

    @Test
    void updateMessage_shouldFail_wrongId() {
      String messageId = message.getId().toString();
      MessageUpdateDto dto = new MessageUpdateDto("new Content");
      MessageResponseDto responseDto = mock(MessageResponseDto.class);
      given(messageManagementService.updateMessage(messageId, dto.newContent()))
          .willThrow(new MessageNotFoundException(ErrorCode.MESSAGE_NOT_FOUND));

      //when & then
      assertThatThrownBy(() -> messageFacade.updateMessage(messageId, dto))
          .isInstanceOf(MessageNotFoundException.class);
    }
  }

  @Nested
  class DeleteMessage {

    @Test
    void deleteMessage_shouldCall() {
      // given
      String messageId = message.getId().toString();
      // when
      messageFacade.deleteMessage(messageId);

      then(messageManagementService).should().deleteMessage(messageId);
    }
  }

}
