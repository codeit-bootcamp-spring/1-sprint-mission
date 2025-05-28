package com.spirnt.mission.discodeit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.spirnt.mission.discodeit.dto.channel.ChannelDto;
import com.spirnt.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.spirnt.mission.discodeit.entity.Channel;
import com.spirnt.mission.discodeit.entity.ChannelType;
import com.spirnt.mission.discodeit.entity.ReadStatus;
import com.spirnt.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.mapper.ChannelMapper;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.repository.ReadStatusRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.basic.BasicChannelService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

    @Mock
    private ChannelMapper channelMapper;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private ReadStatusService readStatusService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReadStatusRepository readStatusRepository;

    @InjectMocks
    private BasicChannelService basicChannelService;

    @Test
    @DisplayName("공개 채널 생성 테스트 - 성공")
    void testCreatePublicSuccess() {
        // given
        PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest(
            "Ch1",
            "Test Ch 1");
        Channel channel = new Channel("Ch1", "Test Ch 1", ChannelType.PUBLIC);
        when(channelRepository.save(eq(channel))).thenReturn(channel);
        when(channelMapper.toDto(any(Channel.class), any(), any())).thenAnswer(invocation -> {
            Channel channelArg = invocation.getArgument(0);
            return new ChannelDto(channelArg.getId(), channelArg.getName(),
                channelArg.getDescription(),
                channelArg.getType(), null, null);
        });

        // when
        ChannelDto result = basicChannelService.createChannelPublic(publicChannelCreateRequest);
        // then
        // 결과값 확인
        assertNotNull(result);
        assertEquals(publicChannelCreateRequest.name(), result.getName());
        assertEquals(publicChannelCreateRequest.description(), result.getDescription());
        assertEquals(ChannelType.PUBLIC, result.getType());
        // 호출됐는지 확인
        verify(channelRepository).save(any(Channel.class));
        verify(channelMapper).toDto(eq(channel), any(), any());
    }

    @Test
    @DisplayName("공개 채널 생성 테스트 - 실패: DB 저장 실패")
    void testCreatePublicFail() {
        // given
        PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest(null,
            "Test Ch 1");
        when(channelRepository.save(any(Channel.class))).thenThrow(
            new DataIntegrityViolationException("DB 저장 실패"));

        // when & then
        assertThrows(DataIntegrityViolationException.class, () ->
            basicChannelService.createChannelPublic(publicChannelCreateRequest)
        );
        // save 시도는 해야함
        verify(channelRepository).save(any(Channel.class));
        // save에서 예외가 발생하면 DTO 변환을 시도하면 안됨
        verify(channelMapper, never()).toDto(any(), any(), any());
    }

    @Test
    @DisplayName("비공개 채널 생성 테스트 - 성공")
    void testCreatePrivateSuccess() {
        // given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        PrivateChannelCreateRequest privateChannelCreateRequest = new PrivateChannelCreateRequest(
            List.of(userId1, userId2));
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        when(channelRepository.save(any(Channel.class))).thenReturn(channel);
        when(channelMapper.toDto(any(Channel.class), any(), any())).thenAnswer(invocation -> {
            Channel channelArg = invocation.getArgument(0);
            return new ChannelDto(channelArg.getId(), channelArg.getName(),
                channelArg.getDescription(),
                channelArg.getType(), null, null);
        });

        // when
        ChannelDto result = basicChannelService.createChannelPrivate(privateChannelCreateRequest);
        // then
        // 결과값 확인
        assertNotNull(result);
        assertEquals(ChannelType.PRIVATE, result.getType());
        // 호출됐는지 확인
        verify(channelRepository).save(any(Channel.class));
        verify(channelMapper).toDto(eq(channel), any(), any());
    }

    @Test
    @DisplayName("비공개 채널 생성 테스트 - 실패: DB 저장 실패")
    void testCreatePrivateFail() {
        // given
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        PrivateChannelCreateRequest privateChannelCreateRequest = new PrivateChannelCreateRequest(
            List.of(userId1, userId2));
        when(channelRepository.save(any(Channel.class))).thenThrow(
            new DataIntegrityViolationException("DB 저장 실패"));

        // when & then
        assertThrows(DataIntegrityViolationException.class, () ->
            basicChannelService.createChannelPrivate(privateChannelCreateRequest)
        );
        // save 시도는 해야함
        verify(channelRepository).save(any(Channel.class));
        // save에서 예외가 발생하면 DTO 변환을 시도하면 안됨
        verify(channelMapper, never()).toDto(any(), any(), any());

    }

    @Test
    @DisplayName("채널 수정 테스트 - 성공")
    void testUpdateChannelSuccess() {
        // given
        UUID channelId = UUID.randomUUID();
        PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest(
            "NewName", null);
        Channel channel = new Channel("Original Name", "Original Description", ChannelType.PUBLIC);
        ChannelDto answer = new ChannelDto(channelId, "NewName", "Original Description",
            ChannelType.PUBLIC, null, channel.getCreatedAt());
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));
        when(channelMapper.toDto(any(Channel.class), any(), any()))
            .thenAnswer(invocation -> {
                Channel channelArg = invocation.getArgument(0);

                return new ChannelDto(
                    channelArg.getId(),
                    channelArg.getName(),
                    channelArg.getDescription(),
                    channelArg.getType(),
                    null,
                    null
                );
            });

        // when
        ChannelDto result = basicChannelService.update(channelId, publicChannelUpdateRequest);

        // then
        assertNotNull(result);
        assertEquals(result.getName(), answer.getName());
        assertEquals(result.getDescription(), answer.getDescription());
        verify(channelRepository).findById(channelId);
    }

    @Test
    @DisplayName("채널 수정 테스트 - 실패: 채널이 존재하지 않음")
    void testUpdateChannelFailDueToNotFound() {
        // given
        UUID channelId = UUID.randomUUID();
        PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest(
            "NewName", null);
        when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ChannelNotFoundException.class, () ->
            basicChannelService.update(channelId, publicChannelUpdateRequest)
        );
        verify(channelRepository).findById(channelId);
    }

    @Test
    @DisplayName("채널 삭제 테스트 - 성공")
    void testDeleteChannelSuccess() {
        // given
        UUID channelId = UUID.randomUUID();
        Channel channel = new Channel(null, null, ChannelType.PRIVATE);
        when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));

        // when
        basicChannelService.delete(channelId);

        // then
        verify(channelRepository).findById(channelId);
        verify(channelRepository).delete(channel);

    }

    @Test
    @DisplayName("채널 삭제 테스트 - 실패: 채널이 존재하지 않음")
    void testDeleteChannelFailDueToNotFound() {
        // given
        UUID channelId = UUID.randomUUID();
        when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(ChannelNotFoundException.class, () ->
            basicChannelService.delete(channelId)
        );
        verify(channelRepository).findById(channelId);
    }

    @Test
    @DisplayName("유저로 채널 찾기 테스트 - 성공")
    void testFindByUserIDSuccess() {
        // given
        UUID userId = UUID.randomUUID();
        UUID publicChannelId = UUID.randomUUID();
        UUID privateChannelId = UUID.randomUUID();
        Channel publicChannel = new Channel("Ch 1", "Public Ch 1", ChannelType.PUBLIC);
        Channel privateChannel = new Channel(null, null, ChannelType.PRIVATE);
        ReflectionTestUtils.setField(publicChannel, "id", publicChannelId);
        ReflectionTestUtils.setField(privateChannel, "id", privateChannelId);

        ChannelDto publicDto = new ChannelDto(publicChannel.getId(), publicChannel.getName(),
            publicChannel.getDescription(), publicChannel.getType(), List.of(),
            publicChannel.getCreatedAt());
        ChannelDto privateDto = new ChannelDto(privateChannel.getId(), privateChannel.getName(),
            privateChannel.getDescription(), privateChannel.getType(), List.of(),
            privateChannel.getCreatedAt());

        when(userRepository.existsById(userId)).thenReturn(true);
        // List.of(...)을 리턴하면 불변 리스트라 addAll() 가능하도록 new List 리턴
        when(channelRepository.findAllPublic()).thenReturn(new ArrayList<>(List.of(publicChannel)));
        when(basicChannelService.findPrivateChannelsByUserId(userId)).thenReturn(
            List.of(privateChannel));
        when(channelMapper.toDto(eq(publicChannel), any(), any())).thenReturn(publicDto);
        when(channelMapper.toDto(eq(privateChannel), any(), any())).thenReturn(privateDto);

        ReadStatus readStatus = mock(ReadStatus.class);
        when(readStatus.getChannel()).thenReturn(privateChannel);
        when(readStatusRepository.findAllByUserId(userId)).thenReturn(List.of(readStatus));

        // when
        List<ChannelDto> result = basicChannelService.findAllByUserId(userId);
        // then
        assertEquals(2, result.size());
        // 결과에 제대로 된 dto가 포함되어 있는가?
        assertTrue(result.stream().anyMatch(
            dto -> dto.getId().equals(publicChannelId) && dto.getType()
                .equals(ChannelType.PUBLIC)));
        assertTrue(result.stream().anyMatch(
            dto -> dto.getId().equals(privateChannelId) && dto.getType()
                .equals(ChannelType.PRIVATE)));
        verify(userRepository).existsById(userId);
        verify(channelRepository).findAllPublic();

    }

    @Test
    @DisplayName("유저로 채널 찾기 테스트 - 실패: 유저가 존재하지 않음")
    void testFindByUserIDFailDueToNotFound() {
        // given
        UUID userId = UUID.randomUUID();
        when(userRepository.existsById(userId)).thenReturn(false);
        // when & then
        assertThrows(UserNotFoundException.class,
            () -> basicChannelService.findAllByUserId(userId));
        verify(userRepository).existsById(userId);
    }


}
