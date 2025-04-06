package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private ChannelMapper channelMapper;

    @InjectMocks
    private BasicChannelService channelService;

    @Nested
    @DisplayName("공개 채널 생성 테스트")
    class CreatePublicChannelTest {

        private PublicChannelCreateRequest request;
        private Channel channel;
        private ChannelDto channelDto;

        @BeforeEach
        void setUp() {
            request = new PublicChannelCreateRequest("general");
            channel = new Channel("general", ChannelType.PUBLIC);
            channelDto = new ChannelDto(UUID.randomUUID(), "general", ChannelType.PUBLIC);
        }

        @Test
        @DisplayName("성공: 새로운 공개 채널을 생성한다")
        void createPublicChannelSuccess() {
            // Given
            given(channelRepository.save(any(Channel.class))).willReturn(channel);
            given(channelMapper.toDto(channel)).willReturn(channelDto);

            // When
            ChannelDto result = channelService.create(request);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo(request.name());
            assertThat(result.type()).isEqualTo(ChannelType.PUBLIC);
            then(channelRepository).should().save(any(Channel.class));
        }
    }

    @Nested
    @DisplayName("비공개 채널 생성 테스트")
    class CreatePrivateChannelTest {

        private PrivateChannelCreateRequest request;
        private Channel channel;
        private ChannelDto channelDto;

        @BeforeEach
        void setUp() {
            request = new PrivateChannelCreateRequest(UUID.randomUUID());
            channel = new Channel(ChannelType.PRIVATE);
            channelDto = new ChannelDto(UUID.randomUUID(), null, ChannelType.PRIVATE);
        }

        @Test
        @DisplayName("성공: 새로운 비공개 채널을 생성한다")
        void createPrivateChannelSuccess() {
            // Given
            given(channelRepository.save(any(Channel.class))).willReturn(channel);
            given(channelMapper.toDto(channel)).willReturn(channelDto);

            // When
            ChannelDto result = channelService.create(request);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.type()).isEqualTo(ChannelType.PRIVATE);
            then(channelRepository).should().save(any(Channel.class));
        }
    }

    @Nested
    @DisplayName("채널 수정 테스트")
    class UpdateTest {

        private UUID channelId;
        private PublicChannelUpdateRequest request;
        private Channel channel;
        private ChannelDto channelDto;

        @BeforeEach
        void setUp() {
            channelId = UUID.randomUUID();
            request = new PublicChannelUpdateRequest("new-general");
            channel = new Channel("general", ChannelType.PUBLIC);
            channelDto = new ChannelDto(channelId, "new-general", ChannelType.PUBLIC);
        }

        @Test
        @DisplayName("성공: 공개 채널 정보를 수정한다")
        void updateSuccess() {
            // Given
            given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));
            given(channelMapper.toDto(channel)).willReturn(channelDto);

            // When
            ChannelDto result = channelService.update(channelId, request);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo(request.name());
            then(channelRepository).should().findById(channelId);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 채널 수정 시도")
        void updateFailWithNonExistentChannel() {
            // Given
            given(channelRepository.findById(channelId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> channelService.update(channelId, request))
                .isInstanceOf(ChannelException.ChannelNotFoundException.class);
        }

        @Test
        @DisplayName("실패: 비공개 채널 수정 시도")
        void updateFailWithPrivateChannel() {
            // Given
            Channel privateChannel = new Channel(ChannelType.PRIVATE);
            given(channelRepository.findById(channelId)).willReturn(Optional.of(privateChannel));

            // When & Then
            assertThatThrownBy(() -> channelService.update(channelId, request))
                .isInstanceOf(ChannelException.PrivateChannelUpdateException.class);
        }
    }

    @Nested
    @DisplayName("채널 삭제 테스트")
    class DeleteTest {

        private UUID channelId;

        @BeforeEach
        void setUp() {
            channelId = UUID.randomUUID();
        }

        @Test
        @DisplayName("성공: 채널을 삭제한다")
        void deleteSuccess() {
            // Given
            given(channelRepository.existsById(channelId)).willReturn(true);

            // When
            channelService.delete(channelId);

            // Then
            then(channelRepository).should().deleteById(channelId);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 채널 삭제 시도")
        void deleteFailWithNonExistentChannel() {
            // Given
            given(channelRepository.existsById(channelId)).willReturn(false);

            // When & Then
            assertThatThrownBy(() -> channelService.delete(channelId))
                .isInstanceOf(ChannelException.ChannelNotFoundException.class);

            then(channelRepository).should(never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("사용자별 채널 조회 테스트")
    class FindByUserIdTest {

        private UUID userId;
        private List<Channel> channels;
        private List<ChannelDto> channelDtos;

        @BeforeEach
        void setUp() {
            userId = UUID.randomUUID();
            channels = List.of(
                new Channel("general", ChannelType.PUBLIC),
                new Channel(ChannelType.PRIVATE)
            );
            channelDtos = List.of(
                new ChannelDto(UUID.randomUUID(), "general", ChannelType.PUBLIC),
                new ChannelDto(UUID.randomUUID(), null, ChannelType.PRIVATE)
            );
        }

        @Test
        @DisplayName("성공: 사용자가 속한 모든 채널을 조회한다")
        void findByUserIdSuccess() {
            // Given
            given(channelRepository.findAllByUserId(userId)).willReturn(channels);
            given(channelMapper.toDto(channels.get(0))).willReturn(channelDtos.get(0));
            given(channelMapper.toDto(channels.get(1))).willReturn(channelDtos.get(1));

            // When
            List<ChannelDto> result = channelService.findAllByUserId(userId);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).type()).isEqualTo(ChannelType.PUBLIC);
            assertThat(result.get(1).type()).isEqualTo(ChannelType.PRIVATE);
            then(channelRepository).should().findAllByUserId(userId);
        }
    }
} 