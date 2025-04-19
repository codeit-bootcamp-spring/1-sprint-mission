package com.sprint.mission.unit.service;

//create, update, delete 메소드
//핵심 메소드에 대해 각각 최소 2개 이상(성공, 실패)의 테스트 케이스를 작성

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.dto.ChannelMapper;
import com.sprint.mission.dto.UserMapper;
import com.sprint.mission.dto.request.ChannelDtoForUpdate;
import com.sprint.mission.dto.request.PrivateChannelCreateDTO;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.dto.response.ChannelDto;
import com.sprint.mission.dto.response.UserDto;
import com.sprint.mission.entity.Channel;
import com.sprint.mission.entity.User;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.ReadStatusRepository;
import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.ReadStatusService;
import com.sprint.mission.service.jcf.serviceImpl.ChannelServiceImpl;
import com.sprint.mission.unit.util.ReflectionFieldSetter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;

import java.time.Instant;
import java.util.*;

import static com.sprint.mission.entity.ChannelType.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.*;

@Import(JPAQueryFactory.class)
@ExtendWith(MockitoExtension.class)
public class ChannelServiceTest {

    private final ReflectionFieldSetter reflectionFieldSetter = new ReflectionFieldSetter();

    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Spy
    private ChannelMapper channelMapper = Mappers.getMapper(ChannelMapper.class);
    @Mock
    private ReadStatusRepository readStatusRepository;
    @Mock
    private MessageService messageService;
    @Mock
    private ReadStatusService readStatusService;
    @Mock
    private ChannelRepository channelRepository;
    @InjectMocks
    private ChannelServiceImpl channelService;

    @Test
    @DisplayName("Public 채널 생성 성공")
    void createPublicChannelSuccess() {
        // given
        PublicChannelCreateDTO dto = new PublicChannelCreateDTO("testChannel", "testChannelName");

        when(channelRepository.save(any(Channel.class)))
                .thenAnswer(invocationOnMock -> reflectionFieldSetter.settingFieldValue(invocationOnMock.getArgument(0)));

        // when
        Channel createdChannel = channelService.createPublicChannel(dto);

        // then
        assertThat(createdChannel).isNotNull();
        assertThat(createdChannel.getName()).isEqualTo(dto.name());
        assertThat(createdChannel.getDescription()).isEqualTo(dto.description());
    }

    @Test
    @DisplayName("Private 채널 생성 성공")
    void createPrivateChannelSuccess() {
        // given
        List<UUID> userIdList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            userIdList.add(UUID.randomUUID());
        }
        PrivateChannelCreateDTO requestDTO = new PrivateChannelCreateDTO(userIdList);
        when(channelRepository.save(any(Channel.class))).thenAnswer(invocation -> {
                    Channel savedChannel = invocation.getArgument(0);
                    reflectionFieldSetter.settingFieldValue(savedChannel);
                    return savedChannel;
                }
        );

        // when
        Channel channel = channelService.createPrivateChannel(requestDTO);

        // then
        assertThat(channel).isNotNull();
        assertThat(channel.getChannelType()).isEqualTo(PRIVATE);
        assertThat(channel.getName()).isNull();
        assertThat(channel.getDescription()).isNull();
    }

    @Test
    @DisplayName("Private 채널 생성 실패 - ReadStatus 생성 실패하면 Private 채널 생성도 실패")
    void createPrivateChannelFail() {
        // given
        List<UUID> userIdList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            userIdList.add(UUID.randomUUID());
        }
        PrivateChannelCreateDTO requestDTO = new PrivateChannelCreateDTO(userIdList);
        when(channelRepository.save(any(Channel.class))).thenAnswer(invocation -> {
                    Channel savedChannel = invocation.getArgument(0);
                    reflectionFieldSetter.settingFieldValue(savedChannel);
                    return savedChannel;
                }
        );
        when(readStatusService.create(any())).thenThrow(new RuntimeException("ReadStatus 생성 실패"));

        // when
        assertThatThrownBy(() -> channelService.createPrivateChannel(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("ReadStatus 생성 실패");
    }


    @Test
    @DisplayName("조회(by ID) 실패 - 채널이 존재하지 않음")
    void findByIdFail() {
        // given
        UUID channelId = UUID.randomUUID();
        when(channelRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> channelService.findById(channelId))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("조회(by ID) 성공")
    void findByIdSuccess() {
        // given
        UUID channelId = UUID.randomUUID();
        Channel channel = new Channel("testChannel", "testChannelName", PUBLIC);
        setField(channel, "id", channelId);
        when(channelRepository.findById(any(UUID.class))).thenReturn(Optional.of(channel));

        // when
        Channel foundChannel = channelService.findById(channelId);

        // then
        assertThat(foundChannel).isNotNull();
        assertThat(foundChannel).isEqualTo(channel);
    }

    @Test
    @DisplayName("모든 채널 조회 성공")
    void findAllSuccess() {
        // given
        int size = 10;
        List<Channel> channels = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            channels.add(new Channel("testChannel" + i, "testChannelName" + i, PUBLIC));
        }
        when(channelRepository.findAll()).thenReturn(channels);

        // when
        List<Channel> foundChannels = channelService.findAll();

        // then
        assertThat(foundChannels).isNotEmpty();
        assertThat(foundChannels.size()).isEqualTo(channels.size());
    }

    @Test
    @DisplayName("특정 유저가 접근 할 수 있는 모든 채널 조회 성공")
    void findAllByUserIdSuccess() {
        // given
        int userCount = 20;
        List<UserDto> userList = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            User user = new User("testUser" + i, "testPassword", "testEmail" + i, null);
            reflectionFieldSetter.settingFieldValue(user);
            userList.add(userMapper.toDto(user));
        }

        int privateChannelCount = 4;
        List<ChannelDto> privateChannelList = new ArrayList<>();
        for (int i = 0; i < privateChannelCount; i++) {
            privateChannelList.add(new ChannelDto(
                    UUID.randomUUID(), PRIVATE, null, null, userList, Instant.now()));
        }

        List<Channel> publicChannelList = new ArrayList<>();
        int publicChannelCount = 5;
        for (int i = 0; i < publicChannelCount; i++) {
            Channel publicChannel = new Channel("testChannel" + i, "testChannelName" + i, PUBLIC);
            reflectionFieldSetter.settingFieldValue(publicChannel);
            publicChannelList.add(publicChannel);
        }

        when(channelRepository.findAllPrivateChannelByUserId(any(UUID.class))).thenReturn(privateChannelList);
        when(channelRepository.findAllByChannelType(any())).thenReturn(publicChannelList);


        // when
        List<ChannelDto> allChannels = channelService.findAllByUserId(UUID.randomUUID());
        List<ChannelDto> privateChannels = allChannels.stream().filter(channelDto -> channelDto.channelType().equals(PRIVATE)).toList();
        List<ChannelDto> publicChannels = allChannels.stream().filter(channelDto -> channelDto.channelType().equals(PUBLIC)).toList();

        // then
        assertThat(allChannels).isNotEmpty();
        assertThat(privateChannels.size()).isEqualTo(privateChannelCount);
        assertThat(publicChannels.size()).isEqualTo(publicChannelCount);
        assertThat(allChannels.size()).isEqualTo(privateChannelCount + publicChannelCount);

        privateChannels.forEach(channelDto ->
                assertThat(channelDto.participants()).isNotEmpty());
    }


    @Test
    @DisplayName("업데이트 성공")
    void updatePrivateChannelSuccess() {
        // given
        ChannelDtoForUpdate requestDTO = new ChannelDtoForUpdate("새로운 이름", "새로운 설명");
        Channel channel = new Channel("testChannel", "testChannelName", PUBLIC);
        reflectionFieldSetter.settingFieldValue(channel);
        when(channelRepository.findById(any(UUID.class))).thenReturn(Optional.of(channel));

        // when
        Channel updatedChannel = channelService.update(channel.getId(), requestDTO);

        // then
        assertThat(updatedChannel).isNotNull();
        assertThat(updatedChannel.getName()).isEqualTo(requestDTO.name());
        assertThat(updatedChannel.getDescription()).isEqualTo(requestDTO.description());
    }

    @Test
    @DisplayName("업데이트 실패 - PRIVATE 채널은 수정할 수 없음")
    void updatePrivateChannelFail() {
        // given
        ChannelDtoForUpdate requestDTO = new ChannelDtoForUpdate("새로운 이름", "새로운 설명");
        Channel channel = new Channel("testChannel", "testChannelName", PRIVATE);
        setField(channel, "id", UUID.randomUUID());
        setField(channel, "createdAt", Instant.now());
        when(channelRepository.findById(any(UUID.class))).thenReturn(Optional.of(channel));

        // when, then
        assertThatThrownBy(() -> channelService.update(channel.getId(), requestDTO))
                .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("삭제 성공 - PUBLIC 채널")
    void deleteSuccessPublic() {
        // given
        UUID channelId = UUID.randomUUID();
        Channel channel = new Channel("testChannel", "testChannelName", PUBLIC);
        when(channelRepository.findById(any(UUID.class))).thenReturn(Optional.of(channel));

        // when
        channelService.delete(channelId);

        // then
        verify(readStatusRepository, times(0)).deleteAllByChannel(channel);
        verify(messageService, times(1)).deleteAllByChannelId(channelId);
        verify(channelRepository, times(1)).delete(channel);
    }

    @Test
    @DisplayName("삭제 성공 - Private 채널")
    void deleteSuccessPrivate() {
        // given
        UUID channelId = UUID.randomUUID();
        Channel channel = new Channel("testChannel", "testChannelName", PRIVATE);
        when(channelRepository.findById(any(UUID.class))).thenReturn(Optional.of(channel));

        // when
        channelService.delete(channelId);

        // then
        verify(readStatusRepository, times(1)).deleteAllByChannel(channel);
        verify(messageService, times(1)).deleteAllByChannelId(channelId);
        verify(channelRepository, times(1)).delete(channel);
    }
}
