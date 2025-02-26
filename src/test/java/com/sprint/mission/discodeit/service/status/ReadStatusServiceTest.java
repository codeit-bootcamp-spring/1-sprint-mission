//package com.sprint.mission.discodeit.service.status;
//
//import com.sprint.mission.discodeit.entity.channel.Channel;
//import com.sprint.mission.discodeit.entity.channel.find.ChannelFindResponse;
//import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
//import com.sprint.mission.discodeit.entity.status.read.ReadStatusCreateRequest;
//import com.sprint.mission.discodeit.entity.status.read.ReadStatusUpdateRequest;
//import com.sprint.mission.discodeit.entity.status.user.UserStatus;
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.entity.user.UserResponse;
//import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
//import com.sprint.mission.discodeit.repository.ReadStatusRepository;
//import com.sprint.mission.discodeit.service.ChannelService;
//import com.sprint.mission.discodeit.service.UserService;
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
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ReadStatusServiceTest {
//
//    @Mock
//    private ReadStatusRepository readStatusRepository;
//
//    @Mock
//    private UserService userService;
//
//    @Mock
//    private ChannelService channelService;
//
//    @InjectMocks
//    private ReadStatusService readStatusService;
//
//    private User user;
//    private User user2;
//    private Channel channel;
//    private Channel channel2;
//    private UUID userId;
//    private UUID channelId;
//    private UserResponse userResponse;
//    private ChannelFindResponse channelResponse;
//
//    @BeforeEach
//    void setUp() {
//        userId = UUID.randomUUID();
//        channelId = UUID.randomUUID();
//
//        user = new User("userA", "userA@email.com", "password");
//        user2 = new User("userB", "userB@email.com", "password");
//        channel = new Channel("publicChannel", user, "PUBLIC", new HashMap<>());
//        channel2 = new Channel("publicChannel2", user, "PUBLIC", new HashMap<>());
//
//        UserStatus userStatus = new UserStatus(userId);
//        userResponse = new UserResponse(user.getUserName(), user.getUserEmail(), userStatus);
//        channelResponse = new ChannelFindResponse(
//                channelId,
//                channel.getChannelName(),
//                channel.getChannelOwnerUser(),
//                channel.getChannelType(),
//                new ReadStatus(userId, channelId)
//        );
//    }
//
//    @Test
//    @DisplayName("ReadStatus 생성 성공")
//    void create() {
//        ReadStatusCreateRequest request = new ReadStatusCreateRequest(userId, channelId);
//        ReadStatus expectedStatus = new ReadStatus(userId, channelId);
//
//        given(channelService.findChannelById(channelId)).willReturn(channelResponse);
//        given(userService.find(userId)).willReturn(userResponse);
//        given(readStatusRepository.findByChannelId(channelId)).willReturn(null);
//        given(readStatusRepository.save(any(ReadStatus.class))).willReturn(expectedStatus);
//
//        ReadStatus result = readStatusService.create(request);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getUserId()).isEqualTo(userId);
//        assertThat(result.getChannelId()).isEqualTo(channelId);
//
//        verify(channelService).findChannelById(channelId);
//        verify(userService).find(userId);
//        verify(readStatusRepository).save(any(ReadStatus.class));
//    }
//
//    @Test
//    @DisplayName("채널 id로 ReadStatus 조회")
//    void findByChannelId() {
//        ReadStatus expectedStatus = new ReadStatus(userId, channelId);
//        given(readStatusRepository.findByChannelId(channelId)).willReturn(expectedStatus);
//
//        ReadStatus result = readStatusService.find(channelId);
//
//        assertThat(result).isEqualTo(expectedStatus);
//        verify(readStatusRepository).findByChannelId(channelId);
//    }
//
//    @Test
//    @DisplayName("userId를 조건으로 모든 ReadStatus 조회")
//    void findByUserId() {
//        Map<UUID, ReadStatus> mockData = new HashMap<>();
//        ReadStatus status1 = new ReadStatus(userId, channelId);
//        ReadStatus status2 = new ReadStatus(userId, UUID.randomUUID());
//        mockData.put(channelId, status1);
//        mockData.put(UUID.randomUUID(), status2);
//
//        given(readStatusRepository.findAll()).willReturn(mockData);
//
//        Map<UUID, ReadStatus> result = readStatusService.findAllByUserId(userId);
//
//        assertThat(result).hasSize(2);
//        verify(readStatusRepository).findAll();
//    }
//
//    @Test
//    @DisplayName("ReadStatus 업데이트")
//    void update() {
//        ReadStatus existingStatus = new ReadStatus(userId, channelId);
//        ReadStatusUpdateRequest updateRequest = new ReadStatusUpdateRequest(channelId);
//
//        given(readStatusRepository.findByChannelId(channelId)).willReturn(existingStatus);
//
//        ReadStatus result = readStatusService.update(updateRequest);
//
//        assertNotNull(result);
//        assertEquals(userId, result.getUserId());
//        verify(readStatusRepository).findByChannelId(channelId);
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 채널로 ReadStatus 생성 시 예외 발생")
//    void createFailWithNonExistentChannel() {
//        ReadStatusCreateRequest request = new ReadStatusCreateRequest(userId, channelId);
//
//        given(channelService.findChannelById(channelId)).willReturn(null);
//
//        assertThrows(ChannelNotFoundException.class, () -> readStatusService.create(request));
//        verify(channelService).findChannelById(channelId);
//        verify(userService, never()).find(any()); // userService는 호출되지 않아야 함
//        verify(readStatusRepository, never()).save(any());
//    }
//
//    @Test
//    @DisplayName("ReadStatus 삭제 성공")
//    void deleteSuccess() {
//        ReadStatus existingStatus = new ReadStatus(userId, channelId);
//        given(readStatusRepository.findByChannelId(channelId)).willReturn(existingStatus);
//        doNothing().when(readStatusRepository).remove(channelId);
//
//        readStatusService.delete(channelId);
//
//        verify(readStatusRepository).findByChannelId(channelId);
//        verify(readStatusRepository).remove(channelId);
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 채널의 ReadStatus 삭제 시도 시 예외 발생")
//    void deleteFailNotFound() {
//        UUID nonExistentChannelId = UUID.randomUUID();
//        given(readStatusRepository.findByChannelId(nonExistentChannelId)).willReturn(null);
//
//        assertThrows(IllegalArgumentException.class,
//                () -> readStatusService.delete(nonExistentChannelId),
//                "존재하지 않는 채널 ID로 삭제 시 예외가 발생해야 합니다");
//
//        verify(readStatusRepository).findByChannelId(nonExistentChannelId);
//        verify(readStatusRepository, never()).remove(any());
//    }
//}