//package com.sprint.mission.discodeit.service.basic;
//
//import com.sprint.mission.discodeit.entity.binarycontent.BinaryContent;
//import com.sprint.mission.discodeit.entity.channel.Channel;
//import com.sprint.mission.discodeit.entity.message.Message;
//import com.sprint.mission.discodeit.entity.message.create.MessageCreateRequest;
//import com.sprint.mission.discodeit.entity.message.MessageSendFileRequest;
//import com.sprint.mission.discodeit.entity.message.MessageUpdateRequest;
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.entity.user.UserRole;
//import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
//import com.sprint.mission.discodeit.exception.channel.IllegalChannelException;
//import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
//import com.sprint.mission.discodeit.exception.message.NullMessageTitleException;
//import com.sprint.mission.discodeit.repository.BinaryContentRepository;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class BasicMessageServiceTest {
//    @Mock
//    private MessageRepository messageRepository;
//    @Mock
//    private BinaryContentRepository binaryContentRepository;
//    @Mock
//    private ChannelRepository channelRepository;
//
//    @InjectMocks
//    private BasicMessageService messageService;
//
//    private User userA;
//    private User userB;
//    private MessageCreateRequest createMessage;
//    private Message message;
//    private Channel channel;
//
//    @BeforeEach
//    void setUp() {
//        userA = new User("userA", "userA@gmail.com", "password", UserRole.ROLE_COMMON);
//        userB = new User("userB", "userB@gmail.com", "password", UserRole.ROLE_SERVER_MANAGER);
//        createMessage = new MessageCreateRequest("Test Title", "Test Content", userA, userB);
//        message = new Message(UUID.randomUUID(), "Test Title", "Test Content", userA, userB);
//
//        Map<UUID, User> userList = new HashMap<>();
//        userList.put(userA.getId(), userA);
//        userList.put(userB.getId(), userB);
//
//        Map<UUID, Message> messages = new HashMap<>();
//        messages.put(message.getId(), message);
//
//        channel = new Channel("test_channel", userA, "PUBLIC", userList);
//        channel.setChannelMessages(messages);
//    }
//
//    @Test
//    @DisplayName("메시지 생성 성공")
//    void createMessageSuccess() {
//        when(messageRepository.saveMessage(any(Message.class))).thenReturn(message);
//
//        Message result = messageService.createMessage(createMessage);
//
//        assertThat(result.getMessageTitle()).isEqualTo(createMessage.title());
//        assertThat(result.getMessageContent()).isEqualTo(createMessage.content());
//        assertThat(result.getMessageSendUser()).isEqualTo(createMessage.sender());
//        assertThat(result.getMessageReceiveUser()).isEqualTo(createMessage.receiver());
//
//        verify(messageRepository, times(1)).saveMessage(any(Message.class));
//    }
//
//    @Test
//    @DisplayName("메시지 생성 실패 - 제목이 null인 경우")
//    void createMessageFailWithNullTitle() {
//        MessageCreateRequest nullTitleRequest = new MessageCreateRequest(null, "Test Content", userA, userB);
//
//        assertThatThrownBy(() -> messageService.createMessage(nullTitleRequest))
//                .isInstanceOf(NullMessageTitleException.class);
//
//        verify(messageRepository, never()).saveMessage(any(Message.class));
//    }
//
//    @Test
//    @DisplayName("첨부 파일이 있는 메시지 생성 성공")
//    void createMessageWithFileSuccess() {
//        MessageSendFileRequest fileRequest = new MessageSendFileRequest(userA.getId(), "test.txt", "txt");
//        when(messageRepository.saveMessage(any(Message.class))).thenReturn(message);
//        when(binaryContentRepository.save(any(BinaryContent.class)))
//                .thenReturn(new BinaryContent(userA.getId(), message.getId(), fileRequest.getFileName(), fileRequest.getFileType()));
//
//        Message result = messageService.createMessage(createMessage, fileRequest);
//
//        assertThat(result).isNotNull();
//        verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));
//        verify(messageRepository, times(1)).saveMessage(any(Message.class));
//    }
//
//    @Test
//    @DisplayName("채널 ID로 메시지 목록 조회 성공")
//    void findAllByChannelIdSuccess() {
//        when(channelRepository.findChannelById(any(UUID.class))).thenReturn(channel);
//
//        Map<UUID, Message> result = messageService.findAllByChannelId(channel.getId());
//
//        assertThat(result).isNotNull();
//        assertThat(result.size()).isEqualTo(1);
//        verify(channelRepository, times(1)).findChannelById(any(UUID.class));
//    }
//
//    @Test
//    @DisplayName("채널 ID로 메시지 목록 조회 실패 - 존재하지 않는 채널")
//    void findAllByChannelIdFailWithNotFoundChannel() {
//        when(channelRepository.findChannelById(any(UUID.class))).thenReturn(null);
//
//        assertThatThrownBy(() -> messageService.findAllByChannelId(UUID.randomUUID()))
//                .isInstanceOf(ChannelNotFoundException.class)
//                .hasMessage("조회하는 채널이 없습니다.");
//    }
//
//    @Test
//    @DisplayName("메시지 업데이트 성공")
//    void updateMessageSuccess() {
//        UUID messageId = message.getId();
//        MessageUpdateRequest updateRequest = new MessageUpdateRequest(messageId, "Updated Title", "Updated Content");
//
//        when(messageRepository.findAllMessage()).thenReturn(Map.of(messageId, message));
//        when(messageRepository.saveMessage(any(Message.class))).thenReturn(message);
//
//        Message result = messageService.updateMessage(updateRequest);
//
//        assertThat(result.getMessageTitle()).isEqualTo("Updated Title");
//        assertThat(result.getMessageContent()).isEqualTo("Updated Content");
//        verify(messageRepository, times(1)).saveMessage(any(Message.class));
//    }
//
//    @Test
//    @DisplayName("메시지 업데이트 실패 - 존재하지 않는 메시지")
//    void updateMessageFailWithNotFoundMessage() {
//        UUID nonExistingId = UUID.randomUUID();
//        MessageUpdateRequest updateRequest = new MessageUpdateRequest(nonExistingId, "Updated Title", "Updated Content");
//
//        when(messageRepository.findAllMessage()).thenReturn(new HashMap<>());
//
//        assertThatThrownBy(() -> messageService.updateMessage(updateRequest))
//                .isInstanceOf(MessageNotFoundException.class);
//    }
//
//    @Test
//    @DisplayName("메시지 삭제 성공")
//    void deleteMessageSuccess() {
//        UUID messageId = message.getId();
//        when(messageRepository.findAllMessage()).thenReturn(Map.of(messageId, message));
//
//        messageService.deleteMessage(messageId);
//
//        verify(binaryContentRepository, times(1)).removeContent(messageId);
//        verify(messageRepository, times(1)).removeMessageById(messageId);
//    }
//
//    @Test
//    @DisplayName("메시지 삭제 실패 - null ID")
//    void deleteMessageFailWithNullId() {
//        assertThatThrownBy(() -> messageService.deleteMessage(null))
//                .isInstanceOf(IllegalChannelException.class)
//                .hasMessage("message ID를 확인해주세요.");
//    }
//}