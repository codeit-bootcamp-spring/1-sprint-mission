package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusRequest.Create;
import com.sprint.mission.discodeit.dto.request.ReadStatusRequest.Update;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.global.exception.readstatus.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.global.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.global.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final ReadStatusMapper readStatusMapper;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ReadStatusResponse create(UUID userId, Create request) {
        UUID channelId = request.getChannelId();

        User user = userRepository.findById(userId).orElseThrow(
            () -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("userId", userId)));

        Channel channel = channelRepository.findById(channelId).orElseThrow(
            () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND,
                Map.of("channelId", channelId)));

        if (readStatusRepository.existsByUserIdAndChannelId(userId, channelId)) {
            throw new ReadStatusAlreadyExistsException(ErrorCode.READ_IS_ALREADY_EXIST,
                Map.of("userId", userId, "channelId", channelId));
        }

        boolean notificationEnabled;

        switch (channel.getType()) {
            case PUBLIC -> notificationEnabled = false;
            case PRIVATE -> notificationEnabled = true;
            default -> throw new IllegalArgumentException("지원하지 않는 채널 타입입니다.");
        }

        ReadStatus newReadStatus = ReadStatus.createReadStatus(user, channel,
            request.getLastReadAt(), notificationEnabled);

        readStatusRepository.save(newReadStatus);

        log.info("Create Read Status : {}", newReadStatus);
        return readStatusMapper.entityToDto(newReadStatus);
    }

    @Override
    public ReadStatusResponse findById(UUID id) {
        return readStatusMapper.entityToDto(findByIdOrThrow(id));
    }

    @Override
    public List<ReadStatusResponse> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
            .map(readStatusMapper::entityToDto)
            .collect(Collectors.toList());
    }

    @Override
    public List<ReadStatusResponse> findAllByChannelId(UUID channelId) {
        return readStatusRepository.findAllByChannelId(channelId).stream()
            .map(readStatusMapper::entityToDto)
            .collect(Collectors.toList());
    }

    @Override
    public ReadStatusResponse update(UUID userId, UUID id, Update request) {
        ReadStatus readStatus = findByIdOrThrow(id);

        boolean hasLastReadAt = request.getNewLastReadAt() != null;
        boolean hasNotificationEnabled = request.getNewNotificationEnabled() != null;

        if (hasLastReadAt == hasNotificationEnabled) {
            throw new IllegalArgumentException("하나의 필드만 업데이트할 수 있습니다.");
        }

        if (hasLastReadAt) {
            if (!readStatus.getUser().getId().equals(userId)) {
                throw new AccessDeniedException("읽음 상태를 수정할 권한이 없습니다.");
            }
            readStatus.updateLastReadAt(request.getNewLastReadAt());
        } else {
            readStatus.updateNotificationEnabled(request.getNewNotificationEnabled());
        }

        return readStatusMapper.entityToDto(readStatusRepository.save(readStatus));
    }

    @Override
    public void deleteById(UUID id) {
        findByIdOrThrow(id);
        readStatusRepository.deleteById(id);
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        readStatusRepository.deleteAllByChannelId(channelId);
    }

    private ReadStatus findByIdOrThrow(UUID id) {
        return readStatusRepository.findById(id)
            .orElseThrow(
                () -> new ReadStatusNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND,
                    Map.of("id", id)));
    }
}
