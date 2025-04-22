package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelMember;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelCanNotModifyException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelMemberRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.sprint.mission.discodeit.exception.ErrorCode.PRIVATE_CANNOT_MODIFY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class BasicChannelServiceUnitTest {

    @InjectMocks
    private BasicChannelService channelService;

    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private ChannelMemberRepository channelMemberRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChannelMapper channelMapper;
    @Mock
    private UserMapper userMapper;

    @Test
    @DisplayName("공개 채널 생성 요청 시 공개 채널을 성공적으로 생성한다.")
    void createPublicChannelSuccess() {
        // given
        UUID userId = UUID.randomUUID();

        PublicChannelRequestDto requestDto = new PublicChannelRequestDto(
                "PublicServerChannel", "public channel", userId
        );

        Channel mappedChannel = mock(Channel.class);

        Channel channel = mock(Channel.class);
        UUID channelId = UUID.randomUUID();
        given(channel.getId()).willReturn(channelId);

        given(channelMapper.toPublicEntity(requestDto)).willReturn(mappedChannel);
        given(channelRepository.saveAndFlush(mappedChannel)).willReturn(channel);
        given(channel.getId()).willReturn(channelId);

        List<UserResponseDto> participants = new ArrayList<>();
        Instant lastMessageAt = Instant.now();

        ChannelResponseDto responseDto = new ChannelResponseDto(
                channelId, Channel.ChannelType.PUBLIC, requestDto.serverName(),
                requestDto.description(), participants, lastMessageAt
        );

        given(channelMapper.toResponseDto(eq(channel), eq(participants), any(Instant.class))).willReturn(responseDto);

        // when
        ChannelResponseDto result = channelService.createPublicChannel(requestDto);
        // then
        assertThat(result).isEqualTo(responseDto);
        assertThat(result.name()).isEqualTo("PublicServerChannel");
        assertThat(result.type()).isEqualTo(Channel.ChannelType.PUBLIC);
    }

    @Test
    @DisplayName("비공개 채널 생성 요청 시 비공개 채널을 성공적으로 생성한다.")
    void createPrivateChannelSuccess() {
        // given
        UUID ownerId = UUID.randomUUID();
        List<UUID> participants = IntStream.range(0, 3)
                .mapToObj(i -> UUID.randomUUID())
                .collect(Collectors.toList());
        PrivateChannelRequestDto requestDto = new PrivateChannelRequestDto(
                ownerId, participants
        );

        UUID channelId = UUID.randomUUID();
        Channel channel = mock(Channel.class);
        Channel savedChannel = mock(Channel.class);
        given(savedChannel.getId()).willReturn(channelId);

        List<UserResponseDto> mockParticipants = List.of(
                mock(UserResponseDto.class),
                mock(UserResponseDto.class),
                mock(UserResponseDto.class)
        );

        List<ChannelMember> mockMembers = mockParticipants.stream()
                .map(dto -> {
                    ChannelMember member = mock(ChannelMember.class);
                    given(member.getUser()).willReturn(mock(User.class));
                    return member;
                }).toList();

        given(savedChannel.getMembers()).willReturn(mockMembers);

        Instant lastMessageAt = Instant.now();
        given(messageRepository.findLastMessageAtByChannelId(savedChannel.getId())).willReturn(lastMessageAt);

        given(channelMapper.toPrivateEntity(requestDto)).willReturn(channel);
        given(channelRepository.save(channel)).willReturn(savedChannel);

        ChannelResponseDto responseDto = new ChannelResponseDto(
                channelId, Channel.ChannelType.PRIVATE, "", "", mockParticipants, lastMessageAt
        );
        given(channelMapper.toResponseDto(eq(savedChannel), anyList(), any(Instant.class))).willReturn(responseDto);

        // when
        ChannelResponseDto result = channelService.createPrivateChannel(requestDto);
        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(channelId);
        assertThat(result.type()).isEqualTo(Channel.ChannelType.PRIVATE);
        assertThat(result.name()).isEqualTo("");
        assertThat(result.participants()).hasSize(3); // 개수만 확인
    }

    @Test
    @DisplayName("사용자 아이디를 받아 사용자가 참여하고 있는 채널 및 공개 채널 전체를 조회한다.")
    void findAllByUserIdSuccess() {
        // given
        UUID userId = UUID.randomUUID();
        
        List<Channel> channelList = List.of(
                mock(Channel.class),
                mock(Channel.class),
                mock(Channel.class)
        );
        given(channelRepository.findAllByUserId(userId)).willReturn(channelList);

        List<ChannelResponseDto> channelResponseDtos = new ArrayList<>();
        for (int i = 0; i < channelList.size(); i++) {
            Channel channel = channelList.get(i);
            UUID channelId = UUID.randomUUID();
            given(channel.getId()).willReturn(channelId);

            User userMock = mock(User.class);
            ChannelMember member = mock(ChannelMember.class);
            given(member.getUser()).willReturn(userMock);
            given(channel.getMembers()).willReturn(List.of(member));

            UserResponseDto userDto = mock(UserResponseDto.class);
            given(userMapper.toResponseDto(userMock)).willReturn(userDto);

            Instant messageTime = Instant.now();
            given(messageRepository.findLastMessageAtByChannelId(channelId)).willReturn(messageTime);

            ChannelResponseDto dto = mock(ChannelResponseDto.class);
            lenient().when(channelMapper.toResponseDto(eq(channel), anyList(), eq(messageTime))).thenReturn(dto);
            channelResponseDtos.add(dto);
        }

        // when
        List<ChannelResponseDto> result = channelService.findAllByUserId(userId);

        // then
        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyElementsOf(channelResponseDtos);
    }

    @Test
    @DisplayName("공개 채널의 ID 및 업데이트 요청 데이터를 받아 채널 업데이트를 성공적으로 수행한다.")
    void updateChannelSuccess() {
        // given
        UUID channelId = UUID.randomUUID();
        Channel channel = mock(Channel.class);
        given(channel.getId()).willReturn(channelId);
        given(channel.getChannelType()).willReturn(Channel.ChannelType.PUBLIC);

        ChannelUpdateDto requestDto = new ChannelUpdateDto(
                "changeServerName", "changeDescription"
        );

        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

        willDoNothing().given(channel).updateServerName(requestDto.name());
        willDoNothing().given(channel).updateDescription(requestDto.description());

        List<UserResponseDto> participants = List.of(
                mock(UserResponseDto.class),
                mock(UserResponseDto.class)
        );
        given(channel.getMembers()).willReturn(List.of(
                mock(ChannelMember.class),
                mock(ChannelMember.class)
        ));
        for (ChannelMember member : channel.getMembers()) {
            User user = mock(User.class);
            given(member.getUser()).willReturn(user);
            given(userMapper.toResponseDto(user)).willReturn(mock(UserResponseDto.class));
        }

        Instant lastMessageAt = Instant.now();
        given(messageRepository.findLastMessageAtByChannelId(channelId)).willReturn(lastMessageAt);

        ChannelResponseDto responseDto = new ChannelResponseDto(
                channelId, Channel.ChannelType.PUBLIC,
                requestDto.name(), requestDto.description(),
                participants, lastMessageAt
        );
        given(channelMapper.toResponseDto(eq(channel), anyList(), any(Instant.class)))
                .willReturn(responseDto);

        // when
        ChannelResponseDto result = channelService.update(channelId, requestDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(channelId);
        assertThat(result.name()).isEqualTo("changeServerName");
        assertThat(result.description()).isEqualTo("changeDescription");
        assertThat(result.participants()).hasSize(2);
    }

    @Test
    @DisplayName("비공개 채널은 수정할 수 없어 비공개 채널을 수정하려고 하면 예외를 발생시킨다.")
    void updateChannelFailedCauseChannelType() {
        // given
        UUID channelId = UUID.randomUUID();
        Channel channel = mock(Channel.class);
        given(channel.getId()).willReturn(channelId);
        given(channel.getChannelType()).willReturn(Channel.ChannelType.PRIVATE);

        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

        ChannelUpdateDto requestDto = mock(ChannelUpdateDto.class);

        // when & then
        assertThatThrownBy(() -> channelService.update(channelId, requestDto))
                .isInstanceOf(PrivateChannelCanNotModifyException.class)
                .hasMessage(PRIVATE_CANNOT_MODIFY.getMessage());
    }

}