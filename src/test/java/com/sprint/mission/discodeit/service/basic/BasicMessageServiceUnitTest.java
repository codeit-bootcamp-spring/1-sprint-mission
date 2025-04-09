package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.BDDAssertions.catchThrowable;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class BasicMessageServiceUnitTest {

    @InjectMocks
    private BasicMessageService messageService;

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private BinaryContentService binaryContentService;

    @Mock
    private MessageMapper messageMapper;
    @Mock
    private PageResponseMapper<MessageResponseDto> pageResponseMapper;

    @Test
    @DisplayName("첨부파일이 존재하지 않는 메시지 생성을 성공적으로 수행한다.")
    void createMessageSuccess() throws IOException {
        // given
        UUID channelId = UUID.randomUUID();

        UUID authorId = UUID.randomUUID();
        UserResponseDto author = mock(UserResponseDto.class);

        UUID messageId = UUID.randomUUID();
        Message message = mock(Message.class);
        Message savedMessage = mock(Message.class);
        given(savedMessage.getId()).willReturn(messageId);

        MessageRequestDto requestDto = new MessageRequestDto(
                "messageContent", authorId, channelId
        );

        MessageResponseDto responseDto = new MessageResponseDto(
                messageId, Instant.now(), Instant.now(), "messageContent",
                channelId, author, List.of()
        );

        given(messageMapper.toEntity(requestDto)).willReturn(message);
        given(messageRepository.save(message)).willReturn(savedMessage);
        given(messageMapper.toResponseDto(savedMessage)).willReturn(responseDto);

        // when
        MessageResponseDto result = messageService.create(requestDto, List.of());

        // then
        Assertions.assertThat(result).isEqualTo(responseDto);
        Assertions.assertThat(result.id()).isEqualTo(messageId);
        Assertions.assertThat(result.content()).isEqualTo("messageContent");
        Assertions.assertThat(result.author()).isEqualTo(author);
    }

    @Test
    @DisplayName("첨부파일이 존재하는 메시지 생성을 성공적으로 수행한다.")
    void createMessageWithFileSuccess() throws IOException {
        // given
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();
        UUID messageId = UUID.randomUUID();
        UserResponseDto author = mock(UserResponseDto.class);

        Message message = mock(Message.class);
        List<MessageAttachment> attachmentsList = new ArrayList<>();
        given(message.getAttachments()).willReturn(attachmentsList);

        Message savedMessage = mock(Message.class);
        given(savedMessage.getId()).willReturn(messageId);

        List<MultipartFile> attachments = List.of(
                new MockMultipartFile("attach1", "attach1.png", "image/png", new byte[]{1, 2, 3}),
                new MockMultipartFile("attach2", "attach2.png", "image/png", new byte[]{1, 2, 3}),
                new MockMultipartFile("attach3", "attach3.png", "image/png", new byte[]{1, 2, 3})
        );

        List<BinaryContentResponseDto> binaryContentResponseDtos = new ArrayList<>();

        MessageRequestDto requestDto = new MessageRequestDto(
                "messageContent", authorId, channelId
        );

        given(messageMapper.toEntity(requestDto)).willReturn(message);
        for (MultipartFile attachment : attachments) {
            BinaryContentResponseDto binaryDto = new BinaryContentResponseDto(
                    UUID.randomUUID(),
                    attachment.getOriginalFilename(),
                    attachment.getSize(),
                    attachment.getContentType(),
                    attachment.getBytes()
            );
            given(binaryContentService.create(attachment)).willReturn(binaryDto);
            given(binaryContentRepository.findById(binaryDto.id())).willReturn(Optional.of(mock(BinaryContent.class)));
        }
        given(messageRepository.save(message)).willReturn(savedMessage);

        MessageResponseDto responseDto = new MessageResponseDto(
                messageId, Instant.now(), Instant.now(), "messageContent",
                channelId, author, binaryContentResponseDtos
        );
        given(messageMapper.toResponseDto(savedMessage)).willReturn(responseDto);

        // when
        MessageResponseDto result = messageService.create(requestDto, attachments);

        // then
        Assertions.assertThat(result).isEqualTo(responseDto);
        Assertions.assertThat(result.id()).isEqualTo(messageId);
        Assertions.assertThat(result.content()).isEqualTo("messageContent");
        Assertions.assertThat(result.author()).isEqualTo(author);
    }

    @Test
    @DisplayName("메시지 삭제 요청 시 성공적으로 삭제를 수행한다.")
    void deleteMessageSuccess() {
        // given
        UUID messageId = UUID.randomUUID();
        Message deleteMessage = mock(Message.class);
        given(deleteMessage.getId()).willReturn(messageId);

        UUID channelId = UUID.randomUUID();
        UserResponseDto author = mock(UserResponseDto.class);
        List<BinaryContentResponseDto> binaryContentResponseDtos = List.of(
                mock(BinaryContentResponseDto.class),
                mock(BinaryContentResponseDto.class),
                mock(BinaryContentResponseDto.class)
        );

        MessageResponseDto responseDto = new MessageResponseDto(
                messageId, Instant.now(), Instant.now(), "messageContent",
                channelId, author, binaryContentResponseDtos
        );

        given(messageRepository.findById(messageId)).willReturn(Optional.of(deleteMessage));
        willDoNothing().given(messageRepository).delete(deleteMessage);
        given(messageMapper.toResponseDto(deleteMessage)).willReturn(responseDto);

        // when
        MessageResponseDto result = messageService.delete(messageId);

        // then
        Assertions.assertThat(result).isEqualTo(responseDto);
    }

    @Test
    @DisplayName("메시지 삭제 요청 도중 Message Id 를 찾지 못해 예외를 반환한다.")
    void deleteMessageFailedCauseCanNotFoundMessageId() {
        // given
        UUID messageId = UUID.randomUUID();

        willThrow(new MessageNotFoundException(messageId))
                .given(messageRepository).findById(messageId);

        // when
        Throwable thrown = catchThrowable(() -> messageService.delete(messageId));
        // then
        Assertions.assertThat(thrown)
                .isInstanceOf(MessageNotFoundException.class)
                .hasMessage(ErrorCode.MESSAGE_NOT_FOUND.getMessage());
    }
}