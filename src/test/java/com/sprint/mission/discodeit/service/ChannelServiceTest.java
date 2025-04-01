package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DomainErrorCode;
import com.sprint.mission.discodeit.exception.RestApiException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Impl.ChannelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @Mock
    private ChannelMapper channelMapper;

    @InjectMocks
    private ChannelServiceImpl channelService;

    private Channel publicChannel;
    private Channel privateChannel;
    private ChannelDto publicChannelDto;
    private ChannelDto privateChannelDto;
    private User testUser1;
    private User testUser2;
    private UUID channelId;
    private UUID userId1;
    private UUID userId2;
    private List<ReadStatus> readStatuses;

    @BeforeEach
    void setUp() {
        channelId = UUID.randomUUID();
        userId1 = UUID.randomUUID();
        userId2 = UUID.randomUUID();
        
        testUser1 = User.builder()
                .name("User 1")
                .email("user1@example.com")
                .password("password1")
                .build();
        testUser1.setId(userId1);
        
        testUser2 = User.builder()
                .name("User 2")
                .email("user2@example.com")
                .password("password2")
                .build();
        testUser2.setId(userId2);

        publicChannel = Channel.builder()
                .name("Public Channel")
                .description("Test Public Channel")
                .type(ChannelType.PUBLIC)
                .build();
        publicChannel.setId(channelId);

        privateChannel = Channel.builder()
                .name("User 1, User 2")
                .description("비공개 채널")
                .type(ChannelType.PRIVATE)
                .build();
        privateChannel.setId(channelId);

        List<UserDto> participants = Arrays.asList(
            UserDto.builder().id(userId1).name("User 1").username("User 1").build(),
            UserDto.builder().id(userId2).name("User 2").username("User 2").build()
        );

        publicChannelDto = ChannelDto.builder()
                .id(channelId)
                .name("Public Channel")
                .description("Test Public Channel")
                .type("PUBLIC")
                .participants(new ArrayList<>())
                .build();

        privateChannelDto = ChannelDto.builder()
                .id(channelId)
                .name("User 1, User 2")
                .description("비공개 채널")
                .type("PRIVATE")
                .participants(participants)
                .build();

        ReadStatus readStatus1 = ReadStatus.builder()
                .user(testUser1)
                .channel(privateChannel)
                .build();
        readStatus1.setId(UUID.randomUUID());

        ReadStatus readStatus2 = ReadStatus.builder()
                .user(testUser2)
                .channel(privateChannel)
                .build();
        readStatus2.setId(UUID.randomUUID());

        readStatuses = Arrays.asList(readStatus1, readStatus2);
    }

    @Test
    @DisplayName("공개 채널 생성 성공 테스트")
    void createPublicChannel_Success() {
        // Given
        PublicChannelCreateRequest request = new PublicChannelCreateRequest();
        request.setName("New Public Channel");
        request.setDescription("New Public Channel Description");
        
        given(channelRepository.save(any(Channel.class))).willReturn(publicChannel);
        given(channelMapper.toDto(any(Channel.class))).willReturn(publicChannelDto);

        // When
        ChannelDto result = channelService.createPublicChannel(request);

        // Then
        assertNotNull(result);
        assertEquals(publicChannel.getName(), result.getName());
        assertEquals(publicChannel.getDescription(), result.getDescription());
        assertEquals(publicChannel.getType().toString(), result.getType());
        then(channelRepository).should().save(any(Channel.class));
    }

    @Test
    @DisplayName("비공개 채널 생성 성공 테스트")
    void createPrivateChannel_Success() {
        // Given
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest();
        request.setParticipantIds(Arrays.asList(userId1, userId2));
        
        given(userRepository.findById(userId1)).willReturn(Optional.of(testUser1));
        given(userRepository.findById(userId2)).willReturn(Optional.of(testUser2));
        given(channelRepository.save(any(Channel.class))).willReturn(privateChannel);
        given(channelMapper.toDto(any(Channel.class))).willReturn(privateChannelDto);

        // When
        ChannelDto result = channelService.createPrivateChannel(request);

        // Then
        assertNotNull(result);
        assertEquals(privateChannelDto.getName(), result.getName());
        assertEquals(privateChannelDto.getDescription(), result.getDescription());
        assertEquals(privateChannelDto.getType(), result.getType());
        then(channelRepository).should().save(any(Channel.class));
        then(readStatusRepository).should(times(2)).save(any(ReadStatus.class));
    }

    @Test
    @DisplayName("비공개 채널 생성 실패 테스트 - 참여자 없음")
    void createPrivateChannel_Failure_NoParticipants() {
        // Given
        PrivateChannelCreateRequest request = new PrivateChannelCreateRequest();
        request.setParticipantIds(Collections.emptyList());
        
        // When & Then
        RestApiException exception = assertThrows(RestApiException.class, () -> 
            channelService.createPrivateChannel(request)
        );
        assertEquals(DomainErrorCode.INVALID_INPUT, exception.getErrorCode());
    }

    @Test
    @DisplayName("채널 정보 수정 성공 테스트")
    void updateChannel_Success() {
        // Given
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest();
        request.setNewName("Updated Channel");
        request.setNewDescription("Updated Description");
        
        Channel updatedChannel = Channel.builder()
                .name("Updated Channel")
                .description("Updated Description")
                .type(ChannelType.PUBLIC)
                .build();
        updatedChannel.setId(channelId);
        
        ChannelDto updatedChannelDto = ChannelDto.builder()
                .id(channelId)
                .name("Updated Channel")
                .description("Updated Description")
                .type("PUBLIC")
                .participants(new ArrayList<>())
                .build();
        
        given(channelRepository.findById(channelId)).willReturn(Optional.of(publicChannel));
        given(channelRepository.save(any(Channel.class))).willReturn(updatedChannel);
        given(channelMapper.toDto(any(Channel.class))).willReturn(updatedChannelDto);

        // When
        ChannelDto result = channelService.updateChannel(channelId, request);

        // Then
        assertNotNull(result);
        assertEquals(request.getNewName(), result.getName());
        assertEquals(request.getNewDescription(), result.getDescription());
        then(channelRepository).should().save(any(Channel.class));
    }

    @Test
    @DisplayName("채널 정보 수정 실패 테스트 - 비공개 채널")
    void updateChannel_Failure_PrivateChannel() {
        // Given
        PublicChannelUpdateRequest request = new PublicChannelUpdateRequest();
        request.setNewName("Updated Channel");
        
        given(channelRepository.findById(channelId)).willReturn(Optional.of(privateChannel));

        // When & Then
        RestApiException exception = assertThrows(RestApiException.class, () -> 
            channelService.updateChannel(channelId, request)
        );
        assertEquals(DomainErrorCode.CHANNEL_INVALID_OPERATION, exception.getErrorCode());
    }

    @Test
    @DisplayName("채널 삭제 성공 테스트")
    void deleteChannel_Success() {
        // Given
        willDoNothing().given(channelRepository).deleteById(channelId);

        // When & Then
        assertDoesNotThrow(() -> channelService.delete(channelId));
        then(channelRepository).should().deleteById(channelId);
    }

    @Test
    @DisplayName("사용자별 채널 목록 조회 성공 테스트")
    void findAllByUserId_Success() {
        // Given
        given(userRepository.existsById(userId1)).willReturn(true);
        given(readStatusRepository.findAllByUserId(userId1)).willReturn(readStatuses);
        given(channelMapper.toDto(any(Channel.class))).willReturn(privateChannelDto);

        // When
        List<ChannelDto> results = channelService.findAllByUserId(userId1);

        // Then
        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(privateChannelDto.getName(), results.get(0).getName());
        then(readStatusRepository).should().findAllByUserId(userId1);
    }

    @Test
    @DisplayName("사용자별 채널 목록 조회 실패 테스트 - 사용자 없음")
    void findAllByUserId_Failure_UserNotFound() {
        // Given
        given(userRepository.existsById(userId1)).willReturn(false);

        // When & Then
        RestApiException exception = assertThrows(RestApiException.class, () -> 
            channelService.findAllByUserId(userId1)
        );
        assertEquals(DomainErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
} 