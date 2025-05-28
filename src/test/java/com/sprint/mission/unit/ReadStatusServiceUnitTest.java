//package com.sprint.mission.unit;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.then;
//
//import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusDto;
//import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusDto;
//import com.sprint.mission.discodeit.entity.Channel;
//import com.sprint.mission.discodeit.entity.ReadStatus;
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.exception.DiscodeitException;
//import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
//import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.ReadStatusRepository;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.service.basic.ReadStatusServiceImpl;
//import java.time.Instant;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//public class ReadStatusServiceUnitTest {
//
//  @Mock
//  private ChannelRepository channelRepository;
//  @Mock
//  private UserRepository userRepository;
//  @Mock
//  private ReadStatusRepository readStatusRepository;
//
//  @InjectMocks
//  private ReadStatusServiceImpl readStatusService;
//
//  private ReadStatus readStatus2;
//  private ReadStatus readStatus;
//  private Channel channel;
//  private User user;
//  private User user2;
//
//  @BeforeEach
//  void setUp() {
//    channel = TestEntityFactory.createPublicChannel();
//    user = TestEntityFactory.createUser("u", "u@gmail.com");
//    readStatus = TestEntityFactory.createReadStatus(channel, user);
//
//    user2 = TestEntityFactory.createUser("u2", "u2@gmail.com");
//    readStatus2 = TestEntityFactory.createReadStatus(channel, user2);
//  }
//
//  @Test
//  void createChannel_success_shouldCall() {
//    // given
//    String channelId = channel.getId().toString();
//    String userId = user.getId().toString();
//    Instant time = Instant.now();
//    CreateReadStatusDto dto = new CreateReadStatusDto(channelId, userId, time);
//    given(channelRepository.findById(UUID.fromString(channelId))).willReturn(
//        Optional.ofNullable(channel));
//    given(userRepository.findById(UUID.fromString(userId))).willReturn(Optional.ofNullable(user));
//    given(readStatusRepository.save(any())).willReturn(readStatus);
//
//    // when
////    ReadStatus result = readStatusService.create(dto);
//
//    // then
//    assertThat(result).isEqualTo(readStatus);
//    assertThat(result.getChannel()).isEqualTo(channel);
//    assertThat(result.getUser()).isEqualTo(user);
//    then(readStatusRepository).should().save(any());
//  }
//
//  @Test
//  void createStatus_fail_whenChannelNotFound() {
//    String channelId = UUID.randomUUID().toString();
//    String userId = user.getId().toString();
//    Instant time = Instant.now();
//    CreateReadStatusDto dto = new CreateReadStatusDto(channelId, userId, time);
//    given(channelRepository.findById(UUID.fromString(channelId))).willThrow(
//        ChannelNotFoundException.class);
//
//    // when & then
//    assertThatThrownBy(() -> readStatusService.create(dto))
//        .isInstanceOf(ChannelNotFoundException.class);
//
//    then(userRepository).shouldHaveNoInteractions();
//    then(readStatusRepository).shouldHaveNoInteractions();
//  }
//
//  @Test
//  void createStatus_fail_whenUserNotFound() {
//    String channelId = channel.getId().toString();
//    String userId = UUID.randomUUID().toString();
//    Instant time = Instant.now();
//    CreateReadStatusDto dto = new CreateReadStatusDto(channelId, userId, time);
//    given(channelRepository.findById(UUID.fromString(channelId))).willReturn(
//        Optional.ofNullable(channel));
//    given(userRepository.findById(UUID.fromString(userId)))
//        .willThrow(UserNotFoundException.class);
//
//    // when & then
//    assertThatThrownBy(() -> readStatusService.create(dto))
//        .isInstanceOf(UserNotFoundException.class);
//
//    then(channelRepository).should().findById(any());
//    then(readStatusRepository).shouldHaveNoInteractions();
//  }
//
//  @Test
//  void find_success_shouldCall() {
//    // given
//    String id = readStatus.getId().toString();
//    given(readStatusRepository.findById(UUID.fromString(id))).willReturn(
//        Optional.ofNullable(readStatus));
//
//    // when
//    ReadStatus result = readStatusService.find(id);
//
//    // then
//    assertThat(result).isEqualTo(readStatus);
//    then(readStatusRepository).should().findById(UUID.fromString(id));
//  }
//
//  @Test
//  void find_fail_shouldThrow() {
//    // given
//    String id = UUID.randomUUID().toString();
//    given(readStatusRepository.findById(UUID.fromString(id)))
//        .willThrow(DiscodeitException.class);
//
//    // when & then
//    assertThatThrownBy(() -> readStatusService.find(id))
//        .isInstanceOf(DiscodeitException.class);
//  }
//
//
//  @Test
//  void findAllByUserId_success_shouldCall() {
//    // given
//    String userId = user.getId().toString();
//    String channelId = channel.getId().toString();
//    given(readStatusRepository.findAllByUser_Id(UUID.fromString(userId)))
//        .willReturn(List.of(readStatus));
//    given(readStatusRepository.findAllByChannel_IdIn(List.of(UUID.fromString(channelId))))
//        .willReturn(List.of(readStatus2));
//
//    // when
//    List<ReadStatus> result = readStatusService.findAllByUserId(userId);
//
//    // then
//    assertThat(result).hasSize(2);
//    assertThat(result).containsExactlyInAnyOrder(readStatus, readStatus2);
//  }
//
//  @Test
//  void findAllByUserId_shouldFail_whenInvalidUUID() {
//    // given
//    String invalidUserId = "invalid-id";
//
//    // when & then
//    assertThatThrownBy(() -> readStatusService.findAllByUserId(invalidUserId))
//        .isInstanceOf(Exception.class);
//
//    then(readStatusRepository).shouldHaveNoInteractions();
//  }
//
//
//  @Test
//  void findAllInChannel_success_shouldCall() {
//    // given
//    List<UUID> channelIds = List.of(channel.getId());
//    given(readStatusRepository.findAllByChannel_IdIn(channelIds))
//        .willReturn(List.of(readStatus, readStatus2));
//
//    // when
//    List<ReadStatus> result = readStatusService.findAllInChannel(channelIds);
//
//    // then
//    assertThat(result).hasSize(2);
//    assertThat(result).containsExactlyInAnyOrder(readStatus2, readStatus);
//    then(readStatusRepository).should().findAllByChannel_IdIn(channelIds);
//  }
//
//  @Test
//  void findAllByChannelId_success_shouldCall() {
//    //given
//    String channelId = channel.getId().toString();
//    given(readStatusRepository.findAllByChannel_Id(UUID.fromString(channelId)))
//        .willReturn(List.of(readStatus, readStatus2));
//
//    // when
//    List<ReadStatus> result = readStatusService.findAllByChannelId(channelId);
//
//    // then
//    assertThat(result).hasSize(2);
//    then(readStatusRepository).should().findAllByChannel_Id(UUID.fromString(channelId));
//  }
//
//  @Test
//  void updateById_success_shouldCall() {
//    // given
//    Instant initialTime = readStatus.getLastReadAt();
//    UpdateReadStatusDto updateDto = new UpdateReadStatusDto(Instant.now());
//    String readStatusId = readStatus.getId().toString();
//    given(readStatusRepository.findById(UUID.fromString(readStatusId)))
//        .willReturn(Optional.ofNullable(readStatus));
//    // when
//    ReadStatus result = readStatusService.updateById(updateDto, readStatusId);
//
//    // then
//    assertThat(initialTime.isBefore(result.getLastReadAt())).isTrue();
//    then(readStatusRepository).should().findById(UUID.fromString(readStatusId));
//    then(readStatusRepository).should().save(readStatus);
//  }
//}
