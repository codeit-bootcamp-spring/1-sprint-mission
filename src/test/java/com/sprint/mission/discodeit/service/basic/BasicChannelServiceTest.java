//package com.sprint.mission.discodeit.service.basic;
//
//import com.sprint.mission.discodeit.entity.channel.*;
//import com.sprint.mission.discodeit.entity.message.Message;
//import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
//import com.sprint.mission.discodeit.entity.user.User;
//import com.sprint.mission.discodeit.entity.user.UserRole;
//import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
//import com.sprint.mission.discodeit.exception.channel.IllegalChannelException;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.ReadStatusRepository;
//import com.sprint.mission.discodeit.service.MessageService;
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
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class BasicChannelServiceTest {
//
//    @Mock
//    private ChannelRepository channelRepository;
//
//    @Mock
//    private MessageService messageService;
//
//    @Mock
//    private ReadStatusRepository readStatusRepository;
//
//    @InjectMocks
//    private BasicChannelService channelService;
//
//    private User user;
//    private User user2;
//    private User user3;
//    private Map<UUID, User> normalUsers;
//    private Map<UUID, User> privateUsers;
//    private Channel publicChannel;
//    private Channel privateChannel;
//    private ChannelCommonRequest commonRequest;
//    private ChannelPrivateRequest privateRequest;
//    private ReadStatus readStatus;
//    private UUID channelId;
//    private Message message;
//
//    @BeforeEach
//    void setUp() {
//        channelId = UUID.randomUUID();
//
//        user = new User("userA", "email@gmail.com", "password", UserRole.ROLE_DEV);
//        user2 = new User("userB", "bbb@gmail.com", "password", UserRole.ROLE_COMMON);
//        user3 = new User("userC", "cccc@gmail.com", "password", UserRole.ROLE_ADMIN);
//
//        normalUsers = new HashMap<>();
//        normalUsers.put(user.getId(), user);
//        normalUsers.put(user2.getId(), user2);
//        normalUsers.put(user3.getId(), user3);
//
//        privateUsers = new HashMap<>();
//        privateUsers.put(user2.getId(), user2);
//        privateUsers.put(user3.getId(), user3);
//
//        publicChannel = new Channel(channelId);
//        publicChannel.updateChannelName("channelA");
//        publicChannel.updateOwnerUser(user);
//        publicChannel.setChannelType("PUBLIC");
//        publicChannel.updateChannelUsers(normalUsers);
//
//        privateChannel = new Channel(UUID.randomUUID());
//        privateChannel.updateChannelName("channelB");
//        privateChannel.updateOwnerUser(user2);
//        privateChannel.setChannelType("PRIVATE");
//        privateChannel.updateChannelUsers(privateUsers);
//
//        commonRequest = new ChannelCommonRequest(channelId, "ourChannel", user);
//        privateRequest = new ChannelPrivateRequest(UUID.randomUUID(), "myChannel", user2, "PRIVATE");
//
//        readStatus = new ReadStatus(user.getId(), channelId);
//
//        message = new Message(UUID.randomUUID());
//        message.updateMessageContent("Test message");
//        message.updateSendUser(user);
//        message.updateReceiveUser(user2);
//    }
//
//    @Test
//    @DisplayName("Public 채널 생성")
//    void createPublicChannel() {
//        when(channelRepository.saveChannel(any(Channel.class))).thenReturn(publicChannel);
//        when(readStatusRepository.save(any(ReadStatus.class))).thenReturn(readStatus);
//
//        Channel result = channelService.createPublicChannel(commonRequest, normalUsers);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getChannelType()).isEqualTo("PUBLIC");
//        assertThat(result.getChannelName()).isEqualTo(commonRequest.getChannelName());
//        verify(channelRepository).saveChannel(any(Channel.class));
//        verify(readStatusRepository).save(any(ReadStatus.class));
//    }
//
//    @Test
//    @DisplayName("Private 채널 생성")
//    void createPrivateChannel() {
//        when(channelRepository.saveChannel(any(Channel.class))).thenReturn(privateChannel);
//        when(readStatusRepository.save(any(ReadStatus.class))).thenReturn(readStatus);
//
//        Channel result = channelService.createPrivateChannel(privateRequest, privateUsers);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getChannelType()).isEqualTo("PRIVATE");
//        assertThat(result.getChannelName()).isEqualTo(privateRequest.getChannelName());
//        verify(channelRepository).saveChannel(any(Channel.class));
//        verify(readStatusRepository, times(privateUsers.size() + 1)).save(any(ReadStatus.class));
//    }
//
//    @Test
//    @DisplayName("채널 ID로 찾기")
//    void findChannelById() {
//        UUID channelId = publicChannel.getId();
//        Map<UUID, ReadStatus> readStatuses = new HashMap<>();
//        readStatuses.put(readStatus.getId(), readStatus);
//
//        when(channelRepository.findChannelById(channelId)).thenReturn(publicChannel);
//        when(readStatusRepository.findAll()).thenReturn(readStatuses);
//
//        ChannelFindResponse result = channelService.findChannelById(channelId);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getChannelId()).isEqualTo(channelId);
//        verify(channelRepository).findChannelById(channelId);
//    }
//
//    @Test
//    @DisplayName("모든 채널 조회")
//    void getAllChannels() {
//        Map<UUID, Channel> channels = new HashMap<>();
//        channels.put(publicChannel.getId(), publicChannel);
//        channels.put(privateChannel.getId(), privateChannel);
//
//        Map<UUID, ReadStatus> readStatuses = new HashMap<>();
//        ReadStatus publicStatus = new ReadStatus(user2.getId(), publicChannel.getId());
//        ReadStatus privateStatus = new ReadStatus(user2.getId(), privateChannel.getId());
//        readStatuses.put(publicStatus.getId(), publicStatus);
//        readStatuses.put(privateStatus.getId(), privateStatus);
//
//        when(channelRepository.findAllChannel()).thenReturn(channels);
//        when(readStatusRepository.findAll()).thenReturn(readStatuses);
//        when(channelRepository.findChannelById(publicChannel.getId())).thenReturn(publicChannel);
//        when(channelRepository.findChannelById(privateChannel.getId())).thenReturn(privateChannel);
//
//        Map<UUID, ChannelFindResponse> result = channelService.getAllChannels(user2.getId());
//
//        assertThat(result).hasSize(2);
//    }
//
//    @Test
//    @DisplayName("채널 업데이트")
//    void updateChannel() {
//        UUID channelId = publicChannel.getId();
//        ChannelUpdateRequest updateRequest = new ChannelUpdateRequest(
//                channelId,
//                "newChannelName",
//                "PUBLIC",
//                user2
//        );
//
//        when(channelRepository.findChannelById(channelId)).thenReturn(publicChannel);
//        when(channelRepository.saveChannel(any(Channel.class))).thenReturn(publicChannel);
//
//        ChannelUpdateResponse result = channelService.updateChannel(updateRequest);
//
//        assertThat(result).isNotNull();
//        assertThat(result.channelName()).isEqualTo("newChannelName");
//        verify(channelRepository).saveChannel(any(Channel.class));
//    }
//
//    @Test
//    @DisplayName("Private 채널 업데이트 시 예외 발생")
//    void updatePrivateChannelThrowsException() {
//        UUID channelId = privateChannel.getId();
//        ChannelUpdateRequest updateRequest = new ChannelUpdateRequest(
//                channelId,
//                "newChannelName",
//                "PRIVATE",
//                user2
//        );
//
//        when(channelRepository.findChannelById(channelId)).thenReturn(privateChannel);
//
//        assertThatThrownBy(() -> channelService.updateChannel(updateRequest))
//                .isInstanceOf(IllegalChannelException.class)
//                .hasMessage("PRIVATE 채널은 수정할 수 없습니다.");
//    }
//
//    @Test
//    @DisplayName("채널 삭제 및 관련 도메인 삭제")
//    void removeChannelById() {
//        UUID channelId = publicChannel.getId();
//        Message message = new Message(UUID.randomUUID());
//        Map<UUID, Message> messages = new HashMap<>();
//        messages.put(message.getId(), message);
//        publicChannel.setChannelMessages(messages);
//
//        Map<UUID, ReadStatus> readStatuses = new HashMap<>();
//        readStatuses.put(readStatus.getId(), readStatus);
//
//        when(channelRepository.findChannelById(channelId)).thenReturn(publicChannel);
//        when(readStatusRepository.findAll()).thenReturn(readStatuses);
//
//        channelService.removeChannelById(channelId);
//
//        verify(messageService).deleteMessage(message.getId());
//        verify(channelRepository).removeChannelById(channelId);
//    }
//
//    @Test
//    @DisplayName("채널에 메시지 추가")
//    void addMessageInCh() {
//        UUID channelId = publicChannel.getId();
//        Message message = new Message(UUID.randomUUID());
//        message.updateMessageContent("Test message");
//        message.updateSendUser(user);
//        message.updateReceiveUser(user2);
//
//        publicChannel.setChannelMessages(new HashMap<>());
//        publicChannel.updateChannelUsers(normalUsers);
//
//        ChannelAddMessageRequest request = new ChannelAddMessageRequest(channelId, message);
//
//        when(channelRepository.findChannelById(channelId)).thenReturn(publicChannel);
//
//        channelService.sendMessage(request);
//
//        verify(channelRepository).saveChannel(publicChannel);
//    }
//
//    @Test
//    @DisplayName("Public 채널 생성 시 추가 속성 검증")
//    void createPublicChannelWithDetailedVerification() {
//        when(channelRepository.saveChannel(any(Channel.class))).thenReturn(publicChannel);
//        when(readStatusRepository.save(any(ReadStatus.class))).thenReturn(readStatus);
//
//        Channel result = channelService.createPublicChannel(commonRequest, normalUsers);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getChannelType()).isEqualTo("PUBLIC");
//        assertThat(result.getChannelOwnerUser()).isEqualTo(commonRequest.getOwner());
//        assertThat(result.getChannelUsers()).isEqualTo(normalUsers);
//        assertThat(result.getChannelMessages()).isNotNull().isEmpty();
//    }
//
//    @Test
//    @DisplayName("Private 채널에 대한 사용자 권한 검증")
//    void verifyPrivateChannelUserAccess() {
//        Map<UUID, Channel> channels = new HashMap<>();
//        channels.put(privateChannel.getId(), privateChannel);
//
//        Map<UUID, ReadStatus> readStatuses = new HashMap<>();
//        ReadStatus privateChannelStatus = new ReadStatus(user.getId(), privateChannel.getId());
//        readStatuses.put(privateChannelStatus.getId(), privateChannelStatus);
//
//        when(channelRepository.findAllChannel()).thenReturn(channels);
//        when(readStatusRepository.findAll()).thenReturn(readStatuses);
//        when(channelRepository.findChannelById(privateChannel.getId())).thenReturn(privateChannel);
//
//        Map<UUID, ChannelFindResponse> result = channelService.getAllChannels(user.getId());
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    @DisplayName("채널 업데이트 시 세부 필드 변경 검증")
//    void updateChannelDetailedVerification() {
//        String newChannelName = "updatedChannel";
//        ChannelUpdateRequest updateRequest = new ChannelUpdateRequest(
//                publicChannel.getId(),
//                newChannelName,
//                "PUBLIC",
//                user2
//        );
//
//        when(channelRepository.findChannelById(publicChannel.getId())).thenReturn(publicChannel);
//
//        ChannelUpdateResponse result = channelService.updateChannel(updateRequest);
//
//        assertThat(result.channelName()).isEqualTo(newChannelName);
//        assertThat(result.ownerUserId()).isEqualTo(user2.getId());
//        assertThat(result.channelType()).isEqualTo("PUBLIC");
//    }
//
//    @Test
//    @DisplayName("채널 삭제 시 모든 연관 데이터 삭제 검증")
//    void removeChannelByIdWithDetailedVerification() {
//        UUID channelId = publicChannel.getId();
//        Message message1 = new Message(UUID.randomUUID());
//        Message message2 = new Message(UUID.randomUUID());
//        Map<UUID, Message> messages = new HashMap<>();
//        messages.put(message1.getId(), message1);
//        messages.put(message2.getId(), message2);
//        publicChannel.setChannelMessages(messages);
//
//        Map<UUID, ReadStatus> readStatuses = new HashMap<>();
//        readStatuses.put(readStatus.getId(), readStatus);
//        ReadStatus additionalStatus = new ReadStatus(user2.getId(), channelId);
//        readStatuses.put(additionalStatus.getId(), additionalStatus);
//
//        when(channelRepository.findChannelById(channelId)).thenReturn(publicChannel);
//        when(readStatusRepository.findAll()).thenReturn(readStatuses);
//
//        channelService.removeChannelById(channelId);
//
//        verify(messageService).deleteMessage(message1.getId());
//        verify(messageService).deleteMessage(message2.getId());
//        verify(channelRepository).removeChannelById(channelId);
//        assertThat(readStatuses.values())
//                .filteredOn(status -> status.getChannelId().equals(channelId))
//                .isEmpty();
//    }
//
//    @Test
//    @DisplayName("채널 이름 중복 시 예외 발생 검증")
//    void createChannelWithDuplicateNameThrowsException() {
//        String duplicateChannelName = "duplicateChannel";
//        UUID existingChannelId = UUID.randomUUID();
//        UUID newChannelId = UUID.randomUUID();
//
//        Channel existingChannel = new Channel(existingChannelId);
//        existingChannel.updateChannelName(duplicateChannelName);
//        existingChannel.updateOwnerUser(user2);
//        existingChannel.setChannelType("PUBLIC");
//        Map<UUID, Channel> existingChannels = new HashMap<>();
//        existingChannels.put(existingChannelId, existingChannel);
//
//        ChannelCommonRequest newRequest = new ChannelCommonRequest(
//                newChannelId,
//                duplicateChannelName,  // 같은 이름으로 시도
//                user
//        );
//
//        when(channelRepository.findAllChannel()).thenReturn(existingChannels);
//
//        assertThatThrownBy(() -> channelService.createPublicChannel(newRequest, normalUsers))
//                .isInstanceOf(IllegalChannelException.class)
//                .hasMessage("중복된 채널 이름입니다.");
//
//        verify(channelRepository, never()).saveChannel(any(Channel.class));
//    }
//
//    @Test
//    @DisplayName("채널 찾기 시 빈 메시지 맵 초기화 검증")
//    void findChannelByIdWithEmptyMessageMap() {
//        UUID channelId = publicChannel.getId();
//        publicChannel.setChannelMessages(null);
//
//        Map<UUID, ReadStatus> readStatuses = new HashMap<>();
//        ReadStatus status = new ReadStatus(user.getId(), channelId);
//        readStatuses.put(status.getId(), status);
//
//        when(channelRepository.findChannelById(channelId)).thenReturn(publicChannel);
//        when(readStatusRepository.findAll()).thenReturn(readStatuses);
//
//        ChannelFindResponse result = channelService.findChannelById(channelId);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getChannelId()).isEqualTo(channelId);
//    }
//
//    @Test
//    @DisplayName("ReadStatus가 없는 채널 조회 시 예외 발생")
//    void findChannelWithoutReadStatusThrowsException() {
//        UUID channelId = publicChannel.getId();
//        when(channelRepository.findChannelById(channelId)).thenReturn(publicChannel);
//        when(readStatusRepository.findAll()).thenReturn(new HashMap<>());
//
//        assertThatThrownBy(() -> channelService.findChannelById(channelId))
//                .isInstanceOf(ChannelNotFoundException.class)
//                .hasMessage("시간 정보를 찾지 못했습니다.");
//    }
//
//    @Test
//    @DisplayName("채널 업데이트 시 소유자 변경 검증")
//    void updateChannelOwnerVerification() {
//        ChannelUpdateRequest updateRequest = new ChannelUpdateRequest(
//                publicChannel.getId(),
//                publicChannel.getChannelName(),
//                "PUBLIC",
//                user3
//        );
//
//        when(channelRepository.findChannelById(publicChannel.getId())).thenReturn(publicChannel);
//
//        ChannelUpdateResponse result = channelService.updateChannel(updateRequest);
//
//        assertThat(result.ownerUserId()).isEqualTo(user3.getId());
//        verify(channelRepository).saveChannel(any(Channel.class));
//    }
//}