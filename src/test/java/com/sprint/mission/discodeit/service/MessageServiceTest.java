package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.message.MessageException;
import com.sprint.mission.discodeit.exception.user.UserException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private BasicMessageService messageService;

    @Nested
    @DisplayName("메시지 생성 테스트")
    class CreateTest {

        private UUID userId;
        private UUID channelId;
        private MessageCreateRequest request;
        private User user;
        private Channel channel;
        private Message message;
        private MessageDto messageDto;

        @BeforeEach
        void setUp() {
            userId = UUID.randomUUID();
            channelId = UUID.randomUUID();
            request = new MessageCreateRequest(channelId, userId, "Hello, world!");
            user = new User("testuser", "test@email.com", "password123!", null);
            channel = new Channel("general", ChannelType.PUBLIC);
            message = new Message(user, channel, "Hello, world!");
            messageDto = new MessageDto(UUID.randomUUID(), userId, channelId, "Hello, world!");
        }

        @Test
        @DisplayName("성공: 새로운 메시지를 생성한다")
        void createSuccess() {
            // Given
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
            given(messageRepository.save(any(Message.class))).willReturn(message);
            given(messageMapper.toDto(message)).willReturn(messageDto);

            // When
            MessageDto result = messageService.create(request);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.content()).isEqualTo(request.content());
            assertThat(result.userId()).isEqualTo(userId);
            assertThat(result.channelId()).isEqualTo(channelId);
            then(messageRepository).should().save(any(Message.class));
        }

        @Test
        @DisplayName("실패: 존재하지 않는 사용자로 메시지 생성 시도")
        void createFailWithNonExistentUser() {
            // Given
            given(userRepository.findById(userId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> messageService.create(request))
                .isInstanceOf(UserException.UserNotFoundException.class);

            then(messageRepository).should(never()).save(any(Message.class));
        }

        @Test
        @DisplayName("실패: 존재하지 않는 채널에 메시지 생성 시도")
        void createFailWithNonExistentChannel() {
            // Given
            given(userRepository.findById(userId)).willReturn(Optional.of(user));
            given(channelRepository.findById(channelId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> messageService.create(request))
                .isInstanceOf(ChannelException.ChannelNotFoundException.class);

            then(messageRepository).should(never()).save(any(Message.class));
        }
    }

    @Nested
    @DisplayName("메시지 수정 테스트")
    class UpdateTest {

        private UUID messageId;
        private MessageUpdateRequest request;
        private Message message;
        private MessageDto messageDto;

        @BeforeEach
        void setUp() {
            messageId = UUID.randomUUID();
            request = new MessageUpdateRequest("Updated message");
            User user = new User("testuser", "test@email.com", "password123!", null);
            Channel channel = new Channel("general", ChannelType.PUBLIC);
            message = new Message(user, channel, "Original message");
            messageDto = new MessageDto(messageId, user.getId(), channel.getId(), "Updated message");
        }

        @Test
        @DisplayName("성공: 메시지 내용을 수정한다")
        void updateSuccess() {
            // Given
            given(messageRepository.findById(messageId)).willReturn(Optional.of(message));
            given(messageMapper.toDto(message)).willReturn(messageDto);

            // When
            MessageDto result = messageService.update(messageId, request);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.content()).isEqualTo(request.newContent());
            then(messageRepository).should().findById(messageId);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 메시지 수정 시도")
        void updateFailWithNonExistentMessage() {
            // Given
            given(messageRepository.findById(messageId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> messageService.update(messageId, request))
                .isInstanceOf(MessageException.MessageNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("메시지 삭제 테스트")
    class DeleteTest {

        private UUID messageId;

        @BeforeEach
        void setUp() {
            messageId = UUID.randomUUID();
        }

        @Test
        @DisplayName("성공: 메시지를 삭제한다")
        void deleteSuccess() {
            // Given
            given(messageRepository.existsById(messageId)).willReturn(true);

            // When
            messageService.delete(messageId);

            // Then
            then(messageRepository).should().deleteById(messageId);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 메시지 삭제 시도")
        void deleteFailWithNonExistentMessage() {
            // Given
            given(messageRepository.existsById(messageId)).willReturn(false);

            // When & Then
            assertThatThrownBy(() -> messageService.delete(messageId))
                .isInstanceOf(MessageException.MessageNotFoundException.class);

            then(messageRepository).should(never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("채널별 메시지 조회 테스트")
    class FindByChannelIdTest {

        private UUID channelId;
        private List<Message> messages;
        private List<MessageDto> messageDtos;

        @BeforeEach
        void setUp() {
            channelId = UUID.randomUUID();
            User user = new User("testuser", "test@email.com", "password123!", null);
            Channel channel = new Channel("general", ChannelType.PUBLIC);
            messages = List.of(
                new Message(user, channel, "First message"),
                new Message(user, channel, "Second message")
            );
            messageDtos = List.of(
                new MessageDto(UUID.randomUUID(), user.getId(), channelId, "First message"),
                new MessageDto(UUID.randomUUID(), user.getId(), channelId, "Second message")
            );
        }

        @Test
        @DisplayName("성공: 채널의 모든 메시지를 조회한다")
        void findByChannelIdSuccess() {
            // Given
            given(messageRepository.findAllByChannelId(channelId)).willReturn(messages);
            given(messageMapper.toDto(messages.get(0))).willReturn(messageDtos.get(0));
            given(messageMapper.toDto(messages.get(1))).willReturn(messageDtos.get(1));

            // When
            List<MessageDto> result = messageService.findAllByChannelId(channelId);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).content()).isEqualTo("First message");
            assertThat(result.get(1).content()).isEqualTo("Second message");
            then(messageRepository).should().findAllByChannelId(channelId);
        }
    }
} 