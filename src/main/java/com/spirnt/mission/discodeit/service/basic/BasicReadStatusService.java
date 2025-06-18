package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.spirnt.mission.discodeit.entity.Channel;
import com.spirnt.mission.discodeit.entity.ChannelType;
import com.spirnt.mission.discodeit.entity.ReadStatus;
import com.spirnt.mission.discodeit.entity.User;
import com.spirnt.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.spirnt.mission.discodeit.exception.ReadStatus.ReadStatusAlreadyExistException;
import com.spirnt.mission.discodeit.exception.ReadStatus.ReadStatusNotFoundException;
import com.spirnt.mission.discodeit.exception.User.UserNotFoundException;
import com.spirnt.mission.discodeit.mapper.ReadStatusMapper;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.ReadStatusRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusMapper readStatusMapper;
    private final ReadStatusRepository readStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;


    @Override
    public ReadStatusDto create(ReadStatusCreateRequest readStatusCreateRequest) {
        UUID userId = readStatusCreateRequest.userId();
        UUID channelId = readStatusCreateRequest.channelId();
        Instant lastReadAt = readStatusCreateRequest.lastReadAt();

        // User와 Channel 존재하지 않으면 예외 발생
        Channel channel = channelRepository.findById(channelId)
            .orElseThrow(() -> {
                log.warn("[Creating ReadStatus Failed: Channel with id {} not found]", channelId);
                return new ChannelNotFoundException(Map.of("channelId", channelId));
            });
        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.warn("[Creating ReadStatus Failed: User with id {} not found]", userId);
                return new UserNotFoundException(Map.of("userId", userId));
            });

        //이미 해당 채널-유저를 가진 ReadStatus가 있으면 예외 발생
        if (readStatusRepository.existsByUserIdAndChannelId(userId,
            channelId)) {
            log.warn(
                "[Creating ReadStatus Failed: ReadStatus with userId {} and channelId {} already exists]",
                userId, channelId);
            throw new ReadStatusAlreadyExistException(
                Map.of("userID", userId, "channelId", channelId));

        }
        // PRIVATE 채널은 알림 활성화 기본, PUBLIC 채널은 알림 비활성화 기본
        ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt,
            !channel.getType().equals(ChannelType.PUBLIC));
        readStatusRepository.save(readStatus);

        return readStatusMapper.toDto(readStatus);
    }

    @Override
    public ReadStatusDto find(UUID readStatusId) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
            .orElseThrow(
                () -> new ReadStatusNotFoundException(Map.of("readStatusId", readStatusId)));
        return readStatusMapper.toDto(readStatus);
    }


    @Override
    public List<ReadStatusDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
            .map(readStatusMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public ReadStatusDto update(UUID readStatusId,
        ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusId)
            .orElseThrow(() -> {
                log.warn("[Updaing ReadStatud Failed: ReadStatus with id {} not found]",
                    readStatusId);
                return new ReadStatusNotFoundException(Map.of("readStatusId", readStatusId));
            });
        readStatus.update(readStatusUpdateRequest.newLastReadAt(),
            readStatusUpdateRequest.notificationEnabled());
        return readStatusMapper.toDto(readStatus);
    }

    @Override
    public void delete(UUID id) {
        readStatusRepository.deleteById(id);
    }
}


