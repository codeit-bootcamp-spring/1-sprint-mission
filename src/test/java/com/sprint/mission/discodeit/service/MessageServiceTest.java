package com.sprint.mission.discodeit.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.reponse.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

/**
 * create, update, delete, findByChannelId 메소드 2개 이상(성공, 실패)의 테스트 케이스
 **/

@ExtendWith(MockitoExtension.class)
public class MessageServiceTest {

  @Mock
  MessageRepository messageRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  ChannelRepository channelRepository;

  @Mock
  BinaryContentService binaryContentService;

  @Mock
  BinaryContentMapper binaryContentMapper;

  @Mock
  MessageMapper messageMapper;

  @Mock
  PageResponseMapper pageResponseMapper;

  @Mock
  InputHandler inputHandler;

  @InjectMocks
  BasicMessageService basicMessageService;

  /**
   * 메세지 생성
   **/
  // 성공
  @Test
  void createMessage_Success() {
    /**given**/

    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageCreateRequest messageCreateRequest = new MessageCreateRequest(channelId, authorId,
        "Test Message Content");

    BinaryContentCreateRequest binaryContentCreateRequest1 = new BinaryContentCreateRequest(
        "profile1.jpg", 1024L, "image/jpeg", new byte[]{});
    BinaryContentCreateRequest binaryContentCreateRequest2 = new BinaryContentCreateRequest(
        "profile2.jpg", 1024L, "image/jpeg", new byte[]{});
    List<BinaryContentCreateRequest> binaryContentCreateRequests = List.of(
        binaryContentCreateRequest1, binaryContentCreateRequest2);

    BinaryContentDto binaryContentDto1 = BinaryContentDto.builder().build();
    BinaryContentDto binaryContentDto2 = BinaryContentDto.builder().build();
//    given(binaryContentService.createBinaryContent(binaryContentCreateRequest1)).willReturn(
//        binaryContentDto1);
//    given(binaryContentService.createBinaryContent(binaryContentCreateRequest2)).willReturn(
//        binaryContentDto2);

    BinaryContent binaryContent1 = mock(BinaryContent.class);
    BinaryContent binaryContent2 = mock(BinaryContent.class);
    given(binaryContentMapper.toEntity(binaryContentDto1)).willReturn(binaryContent1);
    given(binaryContentMapper.toEntity(binaryContentDto1)).willReturn(binaryContent2);

    User user = mock(User.class);
    given(userRepository.findById(authorId)).willReturn(Optional.of(user));
    Channel channel = mock(Channel.class);
    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

    MessageDto messageDto = MessageDto.builder()
        .channelId(channelId)
        .content("Test Message Content")
        .build();
    given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

    /**when**/
    MessageDto result = basicMessageService.createMessage(messageCreateRequest,
        binaryContentCreateRequests);

    /**then**/
    then(userRepository).should().findById(authorId);
    then(channelRepository).should().findById(channelId);
    then(binaryContentService).should(times(2))
        .createBinaryContent(any(BinaryContentCreateRequest.class));
    then(binaryContentMapper).should(times(2)).toEntity(any(BinaryContentDto.class));
    then(messageMapper).should().toDto(any(Message.class));

    assertNotNull(result);
    assertEquals("Test Message Content", result.content());
  }

  // 실패 : 유저를 찾지 못함
  @Test
  void createMessage_Fail_UserNotFound() {
    /**given**/
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageCreateRequest messageCreateRequest = new MessageCreateRequest(channelId, authorId,
        "Test Message Content");

    BinaryContentCreateRequest binaryContentCreateRequest1 = new BinaryContentCreateRequest(
        "profile1.jpg", 1024L, "image/jpeg", new byte[]{});
    BinaryContentCreateRequest binaryContentCreateRequest2 = new BinaryContentCreateRequest(
        "profile2.jpg", 1024L, "image/jpeg", new byte[]{});
    List<BinaryContentCreateRequest> binaryContentCreateRequests = List.of(
        binaryContentCreateRequest1, binaryContentCreateRequest2);

    BinaryContentDto binaryContentDto1 = BinaryContentDto.builder().build();
    BinaryContentDto binaryContentDto2 = BinaryContentDto.builder().build();
//    given(binaryContentService.createBinaryContent(binaryContentCreateRequest1)).willReturn(
//        binaryContentDto1);
//    given(binaryContentService.createBinaryContent(binaryContentCreateRequest2)).willReturn(
//        binaryContentDto2);

    BinaryContent binaryContent1 = mock(BinaryContent.class);
    BinaryContent binaryContent2 = mock(BinaryContent.class);
    given(binaryContentMapper.toEntity(binaryContentDto1)).willReturn(binaryContent1);
    given(binaryContentMapper.toEntity(binaryContentDto1)).willReturn(binaryContent2);

    given(userRepository.findById(authorId)).willReturn(Optional.empty());
    // 실패 시나리오
    assertThrows(UserNotFoundException.class, () -> {
      basicMessageService.createMessage(messageCreateRequest, binaryContentCreateRequests);
    });

    /** when & then - 예외 발생 검증 **/
    then(userRepository).should().findById(authorId);
    then(channelRepository).should(never()).findById(channelId);
    then(binaryContentService).should(times(2))
        .createBinaryContent(any(BinaryContentCreateRequest.class));
    then(binaryContentMapper).should(times(2)).toEntity(any(BinaryContentDto.class));
    then(messageMapper).should(never()).toDto(any(Message.class));
  }

  /**
   * 메세지 수정
   **/
  // 성공
  @Test
  void updateMessage_Succeess() {
    /**given**/
    UUID id = UUID.randomUUID();
    MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest("new Message");

    Message message = Message.builder()
        .content("old Message")
        .build();
    given(messageRepository.findById(id)).willReturn(Optional.of(message));

    MessageDto messageDto = MessageDto.builder()
        .content("new Message")
        .build();
    given(messageMapper.toDto(message)).willReturn(messageDto);

    /**when**/
    basicMessageService.updateMessageText(id, messageUpdateRequest);

    /**then**/
    then(messageRepository).should().findById(id);
    then(messageMapper).should().toDto(message);
  }

  // 실패 : 메세지를 찾지 못함
  @Test
  void updateMassage_Fail_MessageNotFound() {
    /**given**/
    UUID id = UUID.randomUUID();
    MessageUpdateRequest messageUpdateRequest = new MessageUpdateRequest("new Message");

    given(messageRepository.findById(id)).willReturn(Optional.empty());

    /**when**/
    assertThrows(MessageNotFoundException.class, () -> {
      basicMessageService.updateMessageText(id, messageUpdateRequest);
    });

    /**then**/
    then(messageRepository).should().findById(id);
    then(messageMapper).should(never()).toDto(any(Message.class));
  }

  /**
   * 메세지 삭제
   **/
  // 성공
  @Test
  void deleteMessage_Success() {
    /**given**/
    given(inputHandler.getYesNOInput()).willReturn("y");

    Message message = mock(Message.class);
    given(messageRepository.findById(any(UUID.class))).willReturn(Optional.of(message));
    BinaryContent binaryContent1 = mock(BinaryContent.class);
    BinaryContent binaryContent2 = mock(BinaryContent.class);
    UUID binaryContentId1 = UUID.randomUUID();
    UUID binaryContentId2 = UUID.randomUUID();
    List<BinaryContent> binaryContents = List.of(binaryContent1, binaryContent2);
    given(message.getAttachments()).willReturn(binaryContents);
    given(binaryContent1.getId()).willReturn(binaryContentId1);
    given(binaryContent2.getId()).willReturn(binaryContentId2);

    // 더미 Id
    UUID id = UUID.randomUUID();
    /**when**/
    basicMessageService.deleteMessageById(id);

    /**then**/
    then(inputHandler).should().getYesNOInput();
    then(messageRepository).should(times(2)).findById(any(UUID.class));
    then(binaryContentService).should(times(2)).deleteBinaryContentById(any(UUID.class));
    then(messageRepository).should().deleteById(any(UUID.class));
  }

  // 실패 : inputHandler.getYesNOInput()이 "n" 또는 다른 값 반환
  @Test
  void deleteMessage_Fail_KeywordNotY() {
    /**given**/
    UUID id = UUID.randomUUID();
    given(inputHandler.getYesNOInput()).willReturn("n");

    /** when & then - 예외 발생 검증 **/
    basicMessageService.deleteMessageById(id);
    then(inputHandler).should().getYesNOInput();
    then(messageRepository).should(never()).findById(any(UUID.class));
    then(binaryContentService).should(never()).deleteBinaryContentById(any(UUID.class));
    then(messageRepository).should(never()).deleteById(any(UUID.class));
  }

  /**
   * 메세지를 채널 Id로 조회
   **/
  // 성공
  @Test
  void findMessageAllByChannelId_Success() {
    /**given**/
    UUID channelId = UUID.randomUUID();
    Sort sort = Sort.by("createDate").ascending();
    Pageable pageable = PageRequest.of(0, 10, sort); // 첫 페이지, 10개

    Channel channel = mock(Channel.class);
    given(channelRepository.findById(any(UUID.class))).willReturn(Optional.of(channel));

    // 메시지 객체와 페이지 설정
    Message message1 = mock(Message.class);
    Message message2 = mock(Message.class);
    Page<Message> messagePage = new PageImpl<>(List.of(message1, message2));

    given(messageRepository.findByChannelId(channelId, pageable)).willReturn(messagePage); // 메시지 조회

    // 메시지 -> 메시지 DTO 변환 (toDto 메서드)
    MessageDto messageDto1 = MessageDto.builder().build();
    MessageDto messageDto2 = MessageDto.builder().build();
    given(messageMapper.toDto(message1)).willReturn(messageDto1);
    given(messageMapper.toDto(message2)).willReturn(messageDto2);

    PageResponse<MessageDto> pageResponse = PageResponse.<MessageDto>builder().build();
    given(pageResponseMapper.fromPage(any(Page.class))).willReturn(pageResponse);

    /**when**/
    PageResponse<MessageDto> result = basicMessageService.findAllByChannelId(channelId, pageable);

    /**then**/
    assertNotNull(result);
    then(channelRepository).should().findById(channelId);
    then(messageRepository).should().findByChannelId(channelId, pageable);
    then(messageMapper).should().toDto(message1);
    then(messageMapper).should().toDto(message2);
    then(pageResponseMapper).should().fromPage(any(Page.class));
  }

  // 실패 : 채널을 찾지 못함
  @Test
  void findMessageAllByChannelId_Fail_ChannelNotFound() {
    /**given**/
    UUID channelId = UUID.randomUUID();
    Sort sort = Sort.by("createDate").ascending();
    Pageable pageable = PageRequest.of(0, 10, sort); // 첫 페이지, 10개

    // 실패 시나리오
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    /**when**/
    assertThrows(ChannelNotFoundException.class, () -> {
      basicMessageService.findAllByChannelId(channelId, pageable);
    });

    /**then**/
    then(channelRepository).should().findById(channelId);
    then(messageRepository).should(never()).findByChannelId(channelId, pageable);
    then(messageMapper).should(never()).toDto(any(Message.class));
    then(messageMapper).should(never()).toDto(any(Message.class));
    then(pageResponseMapper).should(never()).fromPage(any(Page.class));
  }

}
