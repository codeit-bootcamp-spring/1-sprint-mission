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

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.channel.ChannelPrivateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelPublicRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


/**
 * create(PUBLIC, PRIVATE), update, delete, findByUserId 메소드 2개 이상(성공, 실패)의 테스트 케이스
 **/

@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

  @Mock
  ChannelRepository channelRepository;

  @Mock
  UserRepository userRepository;

  @Mock
  MessageRepository messageRepository;

  @Mock
  ChannelMapper channelMapper;

  @Mock
  ReadStatusRepository readStatusRepository;

  @Mock
  ReadStatusService readStatusService;

  @Mock
  InputHandler inputHandler;

  @InjectMocks
  BasicChannelService basicChannelService;

  /**
   * 채널 생성
   **/

  // 공개 채널 생성 성공
  @Test
  void createPublicChannel_Success() {
    /**given**/
    ChannelPublicRequest channelPublicRequest = new ChannelPublicRequest("TestPublicChannel",
        "Public Channel Test");

    // (1) channelRepository.save() 메서드 호출시 channel 반환
    Channel channel = Channel.builder()
        .type(ChannelType.PUBLIC)
        .name(channelPublicRequest.name())
        .description(channelPublicRequest.description())
        .build();

    // 테스트에서 정의한 channel과 basicChannelService.createPublicChannel() 메서드 내부에서 생성되는 Channel 객체는 다른 인스턴스이다.
    given(channelRepository.save(any(Channel.class))).willReturn(channel);

    // (2) channelMapper.toDto(channel) 메서드 호출시 ChannelDto 반환
    given(channelMapper.toDto(any(Channel.class))).willReturn(new ChannelDto(
        channel.getId(),
        ChannelType.PUBLIC,
        channel.getName(),
        channel.getDescription(),
        null, // participants, mapper 클래스에서 불러오는데, 지금은 생성 메서드 확인하는 것이므로
        null // lastMessageAt, 이하 동문
    ));

    /**when**/
    // 해당 메서드에서 새로운 Channel 객체를 생성 -> given() 으로 전달한다.
    // given()으로 any(Channel.class) 입력을 전달하면 .willReturn() 값으로 무조건 반환한다는 것.
    ChannelDto result = basicChannelService.createPublicChannel(channelPublicRequest);

    /**then**/
    then(channelRepository).should().save(any(Channel.class));
    then(channelMapper).should().toDto(any(Channel.class));

    // 결과가 null 인지 아닌지 검증
    assertNotNull(result);
    // 반환한 UserDto가 기대한 값과 일치하는지 검증
    assertEquals("TestPublicChannel", result.name());
    assertEquals("Public Channel Test", result.description());
  }

  // 공개 채널 생성 실패 : name 이 공백이다. 공백인지 확인한다.(검증 자체는 DTO 에서 진행하기 때문에)
  @Test
  void createPublicChannel_Fail_ChannelNameIsBlank() {
    /**given**/
    ChannelPublicRequest channelPublicRequest = new ChannelPublicRequest("",
        "Public Channel Test");

    // (1) channelRepository.save() 메서드 호출시 channel 반환
    Channel channel = Channel.builder()
        .type(ChannelType.PUBLIC)
        .name(channelPublicRequest.name())
        .description(channelPublicRequest.description())
        .build();

    // 테스트에서 정의한 channel과 basicChannelService.createPublicChannel() 메서드 내부에서 생성되는 Channel 객체는 다른 인스턴스이다.
    given(channelRepository.save(any(Channel.class))).willReturn(channel);

    // (2) channelMapper.toDto(channel) 메서드 호출시 ChannelDto 반환
    given(channelMapper.toDto(any(Channel.class))).willReturn(new ChannelDto(
        channel.getId(),
        ChannelType.PUBLIC,
        channel.getName(),
        channel.getDescription(),
        null, // participants, mapper 클래스에서 불러오는데, 지금은 생성 메서드 확인하는 것이므로
        null // lastMessageAt, 이하 동문
    ));

    /**when**/
    ChannelDto result = basicChannelService.createPublicChannel(channelPublicRequest);

    /**then**/
    assertEquals("", result.name());
  }


  @Test
  void createPrivateChannel_Success() {
    /**given**/
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();
    UUID userId3 = UUID.randomUUID();

    ChannelPrivateRequest channelPrivateRequest = new ChannelPrivateRequest(
        List.of(userId1, userId2, userId3)
    );

    Channel channel = mock(Channel.class);
    UUID channelId = UUID.randomUUID();

    given(channelRepository.save(any(Channel.class))).willReturn(channel);

    given(channel.getId()).willReturn(channelId);

    // (1) participantDtos 를 위한 User 객체 가짜 객체 생성
    User user1 = mock(User.class);
    User user2 = mock(User.class);
    User user3 = mock(User.class);

    // userRepository.findById(userId) 호출시 반환
    given(userRepository.findById(any(UUID.class)))
        .willReturn(Optional.of(user1))
        .willReturn(Optional.of(user2))
        .willReturn(Optional.of(user3));

    given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

    List<UserDto> participantDtos = List.of(
        new UserDto(userId1, "testUser1", "test1@example.com", null, true),
        new UserDto(userId2, "testUser2", "test2@example.com", null, true),
        new UserDto(userId3, "testUser3", "test3@example.com", null, true)
    );

    ChannelDto channelDto = new ChannelDto(
        channelId,
        ChannelType.PRIVATE,
        null,
        null,
        participantDtos,
        null
    );
    given(channelMapper.toDto(channel)).willReturn(channelDto);

    /**when**/
    ChannelDto result = basicChannelService.createPrivateChannel(channelPrivateRequest);

    /**then**/
    then(channelRepository).should().save(any(Channel.class));
    then(channelRepository).should(times(3)).findById(any(UUID.class));
    then(readStatusRepository).should(times(3)).save(any(ReadStatus.class));
    then(userRepository).should(times(3)).findById(any(UUID.class));
    then(channelMapper).should().toDto(channel);

    assertNotNull(result);
  }

  // 비공개 채널 생성 실패 : 참가할 유저를 찾지 못했다.
  @Test
  void createPrivateChannel_Fail_UserNotFound() {
    /**given**/
    UUID userId1 = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();
    UUID userId3 = UUID.randomUUID();

    ChannelPrivateRequest channelPrivateRequest = new ChannelPrivateRequest(
        List.of(userId1, userId2, userId3)
    );

    Channel channel = mock(Channel.class);
    UUID channelId = UUID.randomUUID();

    given(channelRepository.save(any(Channel.class))).willReturn(channel);

    given(channel.getId()).willReturn(channelId);

    // 실패 시나리오

    // userRepository.findById(userId) 호출시 반환
    given(userRepository.findById(any(UUID.class))).willReturn(Optional.empty());

    /**when**/
    assertThrows(UserNotFoundException.class, () -> {
      ChannelDto result = basicChannelService.createPrivateChannel(channelPrivateRequest);
    });

    /**then**/
    then(channelRepository).should().save(any(Channel.class));

    // 유저 검색 메서드는 각 유저마다 한 번씩 호출되어야 함
    then(userRepository).should().findById(any(UUID.class)); // 첫 번째 유저

    then(readStatusRepository).should(never()).save(any(ReadStatus.class));
    then(channelMapper).should(never()).toDto(channel);
  }

  /**
   * 채널 수정
   **/
  // 성공
  @Test
  void updateChannel_Success() {
    /**given**/
    ChannelUpdateRequest channelUpdateRequest = new ChannelUpdateRequest(
        "newChannelName",
        "new Channel Description");

    Channel channel = mock(Channel.class);
    // channelRepository.findById() 메서드 호출시 channel 반환
    given(channelRepository.findById(any(UUID.class))).willReturn(Optional.of(channel));
    // basicChannelService 에 전달하기 위한 더미 ID
    // channelRepository.findById(any(UUID.class) 를 given 함에 따라 실질적으로는 의미가 없다.
    UUID id = UUID.randomUUID();

    // channelMapper.toDto() 메서드 호출시 channelDto 반환
    ChannelDto channelDto = ChannelDto.builder()
        .type(channel.getType())
        .name("newChannelName")
        .description("new Channel Description")
        .build();
    given(channelMapper.toDto(any(Channel.class))).willReturn(channelDto);

    /**when**/
    ChannelDto result = basicChannelService.updateChannel(id, channelUpdateRequest);

    /**then**/
    then(channelRepository).should().findById(any(UUID.class));
    then(channelMapper).should().toDto(any(Channel.class));

    assertNotNull(result);
    assertEquals("newChannelName", result.name());
    assertEquals("new Channel Description", result.description());
  }

  // 실패 : 채널을 찾지 못했다.
  @Test
  void updateChannel_Fail_ChannelNotFound() {
    /** given **/

    ChannelUpdateRequest channelUpdateRequest = new ChannelUpdateRequest(
        "newChannelName",
        "new Channel Description");
    // 더미 id
    UUID id = UUID.randomUUID();

    // 존재하지 않은 채널에 대한 시뮬레이션
    given(channelRepository.findById(any(UUID.class))).willReturn(Optional.empty());

    /** when & then - 예외 발생 검증 **/
    assertThrows(ChannelNotFoundException.class, () -> {
      ChannelDto result = basicChannelService.updateChannel(id, channelUpdateRequest);
    });

    then(channelRepository).should().findById(any(UUID.class));
    then(channelMapper).should(never()).toDto(any(Channel.class));
  }

  /**
   * 채널 삭제
   **/
  // 성공
  @Test
  void deleteChannel_Success() {
    /**given**/
    // (1) inputHandler.getYesNOInput() 메서드 호출시 y 반환
    given(inputHandler.getYesNOInput()).willReturn("y");

    Channel channel = mock(Channel.class);
    given(channelRepository.findById(any(UUID.class))).willReturn(Optional.of(channel));
    // 더미 Id
    UUID id = UUID.randomUUID();

    Message message = mock(Message.class);
    given(messageRepository.findByChannelId(any(UUID.class))).willReturn(List.of(message));

    ReadStatus readStatus = mock(ReadStatus.class);
    given(readStatusService.findAllReadStatusEntitiesByUserId(any(UUID.class))).willReturn(
        List.of(readStatus));

    /**when**/
    basicChannelService.deleteChannelById(id);

    /**then**/
    then(channelRepository).should().findById(any(UUID.class));
    then(messageRepository).should().findByChannelId(any(UUID.class));
    then(readStatusService).should().findAllReadStatusEntitiesByUserId(any(UUID.class));
  }

  // 실패 : inputHandler.getYesNOInput()이 "n" 또는 다른 값 반환
  @Test
  void deleteChannel_Fail_KeywordNotY() {
    /**given**/
    given(inputHandler.getYesNOInput()).willReturn("n");

    // 더미 Id
    UUID id = UUID.randomUUID();

    /** when & then - 예외 발생 검증 **/
    basicChannelService.deleteChannelById(id);

    then(channelRepository).should(never()).findById(any(UUID.class));
    then(messageRepository).should(never()).findByChannelId(any(UUID.class));
    then(readStatusService).should(never()).findAllReadStatusEntitiesByUserId(any(UUID.class));
  }

  /**
   * 유저 아이디로 채널 조회
   **/
  // 성공, 값 조회보다 메서드 호출 검증 위주
  @Test
  void findChannelByUserId_Success() {
    /**given**/
    User user = mock(User.class);
    // 더미 id
    UUID userId = UUID.randomUUID();
    given(userRepository.findById(any(UUID.class))).willReturn(Optional.of(user));

    ReadStatus readStatus1 = mock(ReadStatus.class);
    ReadStatus readStatus2 = mock(ReadStatus.class);

    Channel channel1 = mock(Channel.class);
    Channel channel2 = mock(Channel.class);

    // ReadStatus가 Channel을 반환하도록 설정
    // 아래 작업을 하지 않으면 List<Channel> channels 이 null 이된다.
    //  -> given(channelMapper.toDto(any(Channel.class))) 에서 오류가 발생한다.
    given(readStatus1.getChannel()).willReturn(channel1);
    given(readStatus2.getChannel()).willReturn(channel2);

    given(readStatusService.findAllReadStatusEntitiesByUserId(any(UUID.class))).willReturn(
        List.of(readStatus1, readStatus2)
    );

    /* record 는 불변 객체 생성 기능이라 내부적으로 final 이 선언되어 있어서 mock 사용 불가능(프록시 객체를 생성하는 식의 작동 방식을 가져서)*/
    /* mockito-inline 의존성을 가지면 해결이 된다고 하지만, 아래 방식으로 진행합니다. */
    // 더미 userDto
    UserDto userDto = UserDto.builder().build();
    ChannelDto channelDto1 = ChannelDto.builder()
        .type(ChannelType.PRIVATE)
        .participants(List.of(userDto))
        .build();

    ChannelDto channelDto2 = ChannelDto.builder()
        .type(ChannelType.PUBLIC)
        .name("Public")
        .description("Public Channel")
        .build();

    given(channelMapper.toDto(any(Channel.class)))
        .willReturn(channelDto1)
        .willReturn(channelDto2);

    /**when**/
    List<ChannelDto> result = basicChannelService.findAllByUserId(userId);

    /**then**/
    then(userRepository).should().findById(any(UUID.class));
    then(readStatusService).should().findAllReadStatusEntitiesByUserId(any(UUID.class));
    then(channelMapper).should(times(2)).toDto(any(Channel.class));

    assertNotNull(result);
  }

  // 실패 : 유저를 찾지 못했을 때
  @Test
  void findChannelByUserId_Fail_UserNotFound() {
    /**given**/
    // 더미 id
    UUID id = UUID.randomUUID();

    // 실패 시나리오
    given(inputHandler.getYesNOInput()).willReturn("y");
    given(userRepository.findById(id)).willReturn(Optional.empty());

    /** when & then - 예외 발생 검증 **/
    assertThrows(UserNotFoundException.class, () -> {
      basicChannelService.deleteChannelById(id);
    });

    then(userRepository).should().findById(any(UUID.class));
    then(readStatusService).should(never()).findAllReadStatusEntitiesByUserId(any(UUID.class));
    then(channelMapper).should(never()).toDto(any(Channel.class));
  }
}
