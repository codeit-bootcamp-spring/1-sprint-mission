package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ReadStatusRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicReadStatusService implements ReadStatusService {

    private final ReadStatusRepository readStatusRepository;
    private final ReadStatusMapper readStatusMapper;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @PreAuthorize("hasPermission(#id, 'Message', 'CREATE')")
    @Override
    public ReadStatusResponse create(ReadStatusRequest.Create request) {
        UUID userId = request.getUserId();
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
        ReadStatus newReadStatus = ReadStatus.createReadStatus(user, channel,
            request.getLastReadAt());

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

    @PreAuthorize("hasPermission(#id, 'Message', 'UPDATE')")
    @Override
    public ReadStatusResponse update(UUID id, ReadStatusRequest.Update request) {
        ReadStatus readStatus = findByIdOrThrow(id);
        readStatus.updateLastReadAt(request.getNewLastReadAt());
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
