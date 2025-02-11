package service;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.util.FileType;
import com.sprint.mission.discodeit.validator.EntityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BasicMessageServiceTest {

  @Mock
  private MessageRepository messageRepository;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private EntityValidator validator;
  @Mock
  private BinaryContentService binaryContentService;

  @InjectMocks
  private BasicMessageService messageService;

  private Message message1;
  private Message message2;
  private User user1;
  private Channel channel1;
  @BeforeEach
  void setUp(){

    user1 = new User.UserBuilder("username", "pwd", "email@email.com", "01012341234").nickname("nickname").build();
    channel1 = new Channel.ChannelBuilder("channel", Channel.ChannelType.VOICE).serverUUID("server1").build();
    message1 = new Message.MessageBuilder(user1.getUUID(),channel1.getUUID(), "content1").build();
    message2 = new Message.MessageBuilder(user1.getUUID(), channel1.getUUID(), "content2").binaryContentId("binary1").build();

  }

  @Test
  void testCreateMessage_Success(){
    CreateMessageDto messageDto = new CreateMessageDto("user1", "channel1","content1", Collections.emptyList());

    when(validator.findOrThrow(eq(User.class), eq(messageDto.userId()), any(UserNotFoundException.class))).thenReturn(user1);
    when(validator.findOrThrow(eq(Channel.class), eq(messageDto.channelId()), any(ChannelNotFoundException.class))).thenReturn(channel1);

    when(messageRepository.create(any(Message.class))).thenReturn(message1);
    when(binaryContentService.saveBinaryContentsForMessage(any(CreateMessageDto.class), anyString())).thenReturn(Collections.emptyList());
    MessageResponseDto response = messageService.createMessage(messageDto);

    assertThat(response).isNotNull();
    assertThat(response.content()).isEqualTo("content1");

    verifyNoInteractions(binaryContentRepository);
    verify(messageRepository, times(1)).create(any(Message.class));
    verify(validator, times(1)).findOrThrow(eq(User.class), eq(messageDto.userId()), any(UserNotFoundException.class));
    verify(validator, times(1)).findOrThrow(eq(Channel.class), eq(messageDto.channelId()), any( ChannelNotFoundException.class));
  }

  @Test
  void testCreateMessage_Fail_UserNotFound(){
    CreateMessageDto messageDto = new CreateMessageDto("user1", "channel1","content1", Collections.emptyList());

    when(validator.findOrThrow(eq(User.class), eq(messageDto.userId()), any(UserNotFoundException.class))).thenThrow(new UserNotFoundException());

    assertThatThrownBy(() -> messageService.createMessage(messageDto)).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void testCreateMessage_Fail_ChannelNotFound(){
    CreateMessageDto messageDto = new CreateMessageDto("user1", "channel1","content1", Collections.emptyList());

    when(validator.findOrThrow(eq(User.class), eq(messageDto.userId()), any(UserNotFoundException.class))).thenReturn(user1);
    when(validator.findOrThrow(eq(Channel.class), eq(messageDto.channelId()), any(ChannelNotFoundException.class))).thenThrow(new ChannelNotFoundException());

    assertThatThrownBy(() -> messageService.createMessage(messageDto)).isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  void testGetMessageById(){
    when(messageRepository.findById(message1.getUUID())).thenReturn(Optional.ofNullable(message1));

    MessageResponseDto message = messageService.getMessageById(message1.getUUID());

    assertThat(message.messageId()).isEqualTo(message1.getUUID());
    verify(messageRepository, times(1)).findById(message1.getUUID());
  }

  // TODO : 다시작성
  @Test
  void testGetMessagesByChannel(){

    BinaryContent binary1 = new BinaryContent.BinaryContentBuilder("user1", "file1.jpg", FileType.JPG, 1024, new byte[]{1,2,3})
        .messageId(message1.getUUID())
        .channelId(channel1.getUUID())
        .build();

    when(messageRepository.findByChannel(channel1.getUUID())).thenReturn(List.of(message1, message2));
//    when(binaryContentRepository.findByChannel(channel1.getUUID())).thenReturn(List.of(binary1));
    when(binaryContentService.getBinaryContentsFilteredByChannelAndGroupedByMessage(channel1.getUUID())).thenReturn(Map.of(message1.getUUID(),List.of(binary1)));
    List<MessageResponseDto> responseList = messageService.getMessagesByChannel(channel1.getUUID());

    assertThat(responseList).isNotNull();
    assertThat(responseList).hasSize(2);

    MessageResponseDto response1 = responseList.get(0);
    MessageResponseDto response2 = responseList.get(1);
    assertThat(response1.content()).isEqualTo("content1");
    assertThat(response1.data().get(0).data()).hasSize(3);

    assertThat(response2.content()).isEqualTo("content2");
    assertThat(response2.data()).isEmpty();
  }

  //TODO : 다시 작성
  @Test
  void testUpdateMessage_Success(){

    BinaryContentDto binaryDto = new BinaryContentDto("file1", FileType.JPG, 1024, new byte[]{1,2,3});
    BinaryContent newBinaryContent = new BinaryContent.BinaryContentBuilder(user1.getUUID(), binaryDto.fileName(), binaryDto.fileType(), binaryDto.fileSize(), binaryDto.data()).messageId(message1.getUUID()).channelId(channel1.getUUID()).build();

    MessageUpdateDto updateDto = new MessageUpdateDto(message1.getUUID(), user1.getUUID(), "new content", List.of(binaryDto));

    when(validator.findOrThrow(eq(Message.class), eq(message1.getUUID()), any(MessageNotFoundException.class))).thenReturn(message1);
    when(binaryContentService.updateBinaryContentForMessage(eq(message1), eq(updateDto.userId()), any(List.class))).thenReturn(List.of(newBinaryContent));

    MessageResponseDto messageResponse = messageService.updateMessage(updateDto);

    assertThat(messageResponse).isNotNull();
    assertThat(messageResponse.messageId()).isEqualTo(message1.getUUID());
    assertThat(messageResponse.userId()).isEqualTo(user1.getUUID());

    assertThat(messageResponse.data().get(0).fileName()).isEqualTo("file1");
    assertThat(messageResponse.data().get(0).data()).hasSize(3);

    verify(messageRepository, times(1)).update(message1);
  }

  @Test
  void testUpdateMessage_Fail(){
    BinaryContentDto binaryDto = new BinaryContentDto("file1", FileType.JPG, 1024, new byte[]{1,2,3});

    MessageUpdateDto updateDto = new MessageUpdateDto(message1.getUUID(), user1.getUUID(), "new content", List.of(binaryDto));

    when(validator.findOrThrow(eq(Message.class), eq(message1.getUUID()), any(MessageNotFoundException.class))).thenThrow(new MessageNotFoundException());

    assertThatThrownBy(() -> messageService.updateMessage(updateDto)).isInstanceOf(MessageNotFoundException.class);
  }

}
