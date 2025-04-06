package com.sprint.mission.service.jcf.main;


import com.sprint.mission.common.exception.CustomException;
import com.sprint.mission.common.exception.ErrorCode;
import com.sprint.mission.dto.ChannelMapper;
import com.sprint.mission.dto.request.PrivateChannelCreateDTO;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.dto.response.ChannelDto;
import com.sprint.mission.entity.addOn.ReadStatus;
import com.sprint.mission.entity.main.BaseEntity;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.entity.main.Message;
import com.sprint.mission.entity.main.User;
import com.sprint.mission.repository.ChannelRepository;
import com.sprint.mission.repository.MessageRepository;
import com.sprint.mission.repository.ReadStatusRepository;
import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.service.ChannelService;
import com.sprint.mission.dto.request.ChannelDtoForUpdate;

import com.sprint.mission.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.sprint.mission.entity.main.ChannelType.PRIVATE;
import static com.sprint.mission.entity.main.ChannelType.PUBLIC;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;
    private final MessageRepository messageRepository;


    @Override
    public Channel createPublicChannel(PublicChannelCreateDTO request) {
        return channelRepository.save(channelMapper.toPublicEntity(request, PUBLIC));
    }

    @Override
    public Channel createPrivateChannel(PrivateChannelCreateDTO request) {
        Channel createdChannel = channelRepository.save(channelMapper.toPrivateEntity(PRIVATE));
        request.participantIds().stream()
                .map(userId -> {
                    User participatingUser = userRepository.findById(userId)
                            .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_USER));
                    return new ReadStatus(participatingUser, createdChannel, createdChannel.getCreatedAt());
                })// 나중에
                .forEach(readStatusRepository::save);
        return createdChannel;
    }


    /**
     * [ ] 특정 User가 볼 수 있는 Channel 목록을 조회하도록 조회 조건을 추가하고, 메소드 명을 변경합니다. findAllByUserId [ ] PUBLIC 채널
     * 목록은 전체 조회합니다. [ ] PRIVATE 채널은 조회한 User가 참여한 채널만 조회합니다.
     */
    @Transactional(readOnly = true)
    @Override
    public Channel findById(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_CHANNEL));
    }

    @Transactional(readOnly = true)
    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        // 쿼리1
        List<ReadStatus> readStatusList = readStatusRepository.findAllByUser_Id(userId);
        // 유저가 참여한 Private 채널 리스트
        List<Channel> participatingPrivateChannel = readStatusList.stream().map(ReadStatus::getChannel).toList();

        List<ChannelDto> channelDtoList = new ArrayList<>();
        participatingPrivateChannel.forEach((channel)->{
            // 채널별 ReadStauts들 가져오기
            // 쿼리2
            Instant lastMessageAt = messageRepository.findTop1ByChannel_IdOrderByCreatedAtDesc(channel.getId())
                    .map(BaseEntity::getCreatedAt)
                    .orElse(null);

            // 쿼리3
            List<User> userList = readStatusRepository.findAllByChannel_Id(channel.getId()).stream()
                    .map(ReadStatus::getUser).toList();
            channelDtoList.add(channelMapper.toDto(channel, userList, lastMessageAt));
        });

        // 쿼리4
        channelDtoList.addAll(channelRepository.findAllByChannelType(PUBLIC)
                .stream().map(channelMapper::toDto)
                .toList());

        return channelDtoList;
    }

    @Override
    public Channel update(UUID channelId, ChannelDtoForUpdate dto) {
        Channel updatingChannel = this.findById(channelId);
        if (updatingChannel.isPrivate()) {
            throw new CustomException(ErrorCode.CANNOT_UPDATE_PRIVATE_CHANNEL);
        }
        updatingChannel.update(dto.name(), dto.description());
        return updatingChannel;
    }

    @Override
    public void delete(UUID channelId) {
        Channel deletingChannel = this.findById(channelId);

        if (deletingChannel.isPrivate()) {
            readStatusRepository.deleteAllByChannel(deletingChannel);
        }
        messageService.deleteAllByChannelId(channelId);
        channelRepository.delete(deletingChannel);
    }

    /**
     * 중복 검증
     */
    public void validateDuplicateName(String name) {
        boolean isDuplicate = channelRepository.findAll().stream()
                .anyMatch(channel -> channel.getName().equals(name));
        if (isDuplicate) {
            throw new CustomException(ErrorCode.ALREADY_EXIST_NAME);
        }
    }
}

